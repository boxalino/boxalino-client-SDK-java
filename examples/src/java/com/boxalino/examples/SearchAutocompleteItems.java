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

/**
 *
 * @author HASHIR
 */
public class SearchAutocompleteItems extends HttpServlet {

    String account;
    String password;
    String domain;
    List<String> logs;
    String language;
    boolean print = true;
    boolean isDev;
    public BxAutocompleteResponse bxAutocompleteResponse = null;

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
            isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            //Create the Boxalino Data SDK instance

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);
            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "whit"; // a search query to be completed
            int textualSuggestionsHitCount = 10; //a maximum number of search textual suggestions to return in one page
            ArrayList<String> fieldNames = new ArrayList<String>() {

                {
                    add("title");

                }
            }; //return the title for each item returned (globally and per textual suggestion) - IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            //create search request
            BxAutocompleteRequest bxRequest = new BxAutocompleteRequest(language, queryText, textualSuggestionsHitCount, 0, "", "");
            //set the fields to be returned for each item in the response
            bxRequest.getBxSearchRequest().setReturnFields(fieldNames);

            //set the request
            bxClient.setAutocompleteRequest(new ArrayList<BxAutocompleteRequest>() {
                {
                    add(bxRequest);
                }
            });

            // make the query to Boxalino server and get back the response for all requests
            bxAutocompleteResponse = bxClient.getAutocompleteResponse();
            //loop on the search response hit ids and print them
            logs.add("textual suggestions for \"" + queryText + "\":<br>");
            for (String suggestion : bxAutocompleteResponse.getTextualSuggestions()) {
                logs.add("<div style=\"border:1px solid; padding:10px; margin:10px\">");
                logs.add("<h3>" + suggestion + "</b></h3>");

                logs.add("item suggestions for suggestion \"" + suggestion + "\":<br>");
                //loop on the search response hit ids and print them
                for (Map.Entry itemk : bxAutocompleteResponse.getBxSearchResponse(suggestion).getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                    logs.add("<div>" + itemk.getKey() + "");
                    for (Map.Entry fValueMap : ((Map<String, List<String>>) itemk.getValue()).entrySet()) {
                        logs.add(" - " + fValueMap.getKey() + ": " + String.join(",", ((List<String>) fValueMap.getValue())) + "");
                    }
                    logs.add("</div>");
                }
                logs.add("</div>");
            }
            logs.add("global item suggestions for \"" + queryText + "\":<br>");
            //loop on the search response hit ids and print them
            for (Map.Entry fvalueMap : bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                String item = String.valueOf(fvalueMap.getKey());
                for (Map.Entry itemInfieldValueMap : ((Map<String, List<String>>) fvalueMap.getValue()).entrySet()) {
                    item += " - " + itemInfieldValueMap.getKey() + ": " + String.join(",", (List<String>) itemInfieldValueMap.getValue()) + "<br>";
                }
                logs.add(item);
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
