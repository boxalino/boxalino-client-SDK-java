/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import boxalino.client.SDK.BxAutocompleteRequest;
import boxalino.client.SDK.BxAutocompleteResponse;
import boxalino.client.SDK.BxClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;

/**
 *
 * @author HASHIR
 */
public class SearchAutocompleteItemsBundled extends HttpServlet {

    String account;
    String password;
    String domain;
    List<String> logs;
    String language;
    boolean print = true;
    boolean isDev;
    public List<BxAutocompleteResponse> bxAutocompleteResponses = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpContext.request = request;
        HttpContext.response = response;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = ""; // your account name
            password = ""; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>();
            //optional, just used here in example to collect logs
            boolean print = true;
            //Create the Boxalino Data SDK instance

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            List<String> queryTexts = new ArrayList<String>() {
                {
                    add("whit");
                    add("yello");
                }
            }; // a search query to be completed
            int textualSuggestionsHitCount = 10; //a maximum number of search textual suggestions to return in one page
            ArrayList<String> fieldNames = new ArrayList<String>() {
                {

                    add("title");
                }
            }; //return the title for each item returned (globally and per textual suggestion) - IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"

            ArrayList<BxAutocompleteRequest> bxRequests = new ArrayList<BxAutocompleteRequest>();
            for (String queryText : queryTexts) {

                //create search request
                BxAutocompleteRequest bxRequest = new BxAutocompleteRequest(language, queryText, textualSuggestionsHitCount, 0, "", "");

                //N.B.: in case you would want to set a filter on a request and not another, you can simply do it by getting the searchchoicerequest with: $bxRequest->getBxSearchRequest() and adding a filter
                //set the fields to be returned for each item in the response
                bxRequest.getBxSearchRequest().setReturnFields(fieldNames);
                bxRequests.add(bxRequest);
            }
            //set the request
            bxClient.setAutocompleteRequests(bxRequests);

            //make the query to Boxalino server and get back the response for all requests
            bxAutocompleteResponses = bxClient.getAutocompleteResponses();
            int i = -1;
            for (BxAutocompleteResponse bxAutocompleteResponse : bxAutocompleteResponses) {

                //loop on the search response hit ids and print them
                String queryText = queryTexts.get(++i);

                logs.add("<h2>textual suggestions for \"" + queryText + "\":</h2>");
                for (String suggestion : bxAutocompleteResponse.getTextualSuggestions()) {
                    logs.add("<div style=\"border:1px solid; padding:10px; margin:10px\">");

                    logs.add("<h3> " + suggestion + "</b></h3>");

                    logs.add("item suggestions for suggestion  \"" + suggestion + "\" :");

                    // loop on the search response hit ids and print them
                    for (Map.Entry item : bxAutocompleteResponse.getBxSearchResponse(suggestion).getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                        logs.add("<div>" + item.getKey());
                        for (Map.Entry itemFieldValueMap : ((Map<String, List<String>>) item.getValue()).entrySet()) {
                            logs.add(" - " + itemFieldValueMap.getKey() + ": " + String.join(",", (List<String>) itemFieldValueMap.getValue()) + "");
                        }
                        logs.add("</div>");
                    }
                    logs.add("</div>");

                }
                logs.add("<h2>global item suggestions for \"" + queryText + "\":</h2>");
                // loop on the search response hit ids and print them
                for (Map.Entry hitvalue : bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                    logs.add("<div>" + hitvalue.getKey() + "");
                    for (Map.Entry fvaluemap : ((Map<String, List<String>>) hitvalue.getValue()).entrySet()) {
                        logs.add(" - " + fvaluemap.getKey() + ": " + String.join(",", (List<String>) fvaluemap.getValue()) + "");
                    }
                    logs.add("</div>");
                }
            }
            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (UnsupportedEncodingException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (TException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (URISyntaxException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (MalformedURLException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (NoSuchFieldException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
