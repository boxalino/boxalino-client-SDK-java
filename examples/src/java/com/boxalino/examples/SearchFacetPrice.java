/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxFacets;
import boxalino.client.SDK.BxSearchRequest;
import com.boxalino.p13n.api.thrift.FacetValue;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author HASHIR
 */
public class SearchFacetPrice extends HttpServlet {

    public String account;
    public String password;
    public boolean print;
    String domain;
    List<String> logs;
    String language;
    String queryText;
    int hitCount;
    String selectedValue;
    public BxChooseResponse bxResponse = null;
    public BxFacets facets = null;

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
             * In this example, we make a simple search query, request a facet
             * and get the search results and print the facet values and counter
             * for price ranges. We also implement a simple link logic so that
             * if the user clicks on one of the facet values the page is
             * reloaded with the results with this facet value selected.
             */

            //required parameters you should set for this example to work
            String account = "boxalino_automated_tests"; // your account name
            String password = "boxalino_automated_tests"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean isDev = false;
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page
            boolean print = true;
            selectedValue = request.getParameter("bx_price") == null ? request.getParameter("bx_price") : null;

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add a facert
            facets = new BxFacets();
            //facets.addPriceRangeFacet(selectedValue);

            facets.addPriceRangeFacet(selectedValue, 2, "Price", "discountedPrice");
            bxRequest.setFacets(facets);

            //set the fields to be returned for each item in the response
            bxRequest.setReturnFields(new ArrayList<String>() {
                {
                    add(facets.getPriceFieldName());
                }
            });

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //get the facet responses
            facets = bxResponse.getFacets("", true, 0, 10);

            //loop on the search response hit ids and print them
            for (Map.Entry fieldValue : facets.getPriceRanges().entrySet()) {
                String range = "<a href=\"?bx_price=" + facets.getPriceValueParameterValue((FacetValue) fieldValue.getValue()) + "\">" + facets.getPriceValueLabel((FacetValue) fieldValue.getValue()) + "</a> (" + facets.getPriceValueCount((FacetValue) fieldValue.getValue()) + ")";
                if ((facets.isPriceValueSelected((FacetValue) fieldValue.getValue())).isEmpty()) {
                    range += "<a href=\"?\">[X]</a>";
                }
                logs.add(range);
            }

            //loop on the search response hit ids and print them
            for (Map.Entry item : bxResponse.getHitFieldValues(new String[]{facets.getPriceFieldName()}, "", true, 0, 10).entrySet()) {
                logs.add("<h3>" + item.getKey() + "</h3>");
                for (Map.Entry fieldValueMapItem : ((Map<String, List<String>>) item.getValue()).entrySet()) {

                    logs.add("Price: " + String.join(",", (List<String>) fieldValueMapItem.getValue()));
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
