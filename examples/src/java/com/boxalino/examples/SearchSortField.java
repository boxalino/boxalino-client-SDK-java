/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.ServletHttpContext;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxRequest;
import boxalino.client.SDK.BxSearchRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class SearchSortField {

    public String account;
    public String password;
    public boolean print;
    public BxChooseResponse bxResponse = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void searchSortField(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            String account = "csharp_unittest"; // your account name
            String password = "csharp_unittest"; // your account password
            boolean isDev = false;
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs

            /* TODO Instantiate ServletHttpContext to manage cookies.*/
            ServletHttpContext.request = request;
            ServletHttpContext.response = response;
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page
            String sortField = "title"; //sort the search results by this field - IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            boolean sortDesc = true; //sort in an ascending / descending way
            //create search request
            BxRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add a sort field in the provided direction
            bxRequest.addSortField(sortField, sortDesc);

            //set the fields to be returned for each item in the response
            bxRequest.setReturnFields(new ArrayList<String>() {
                {
                    add(sortField);
                }
            });

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //loop on the search response hit ids and print them
            Map<String, Object> HitFieldValues = bxResponse.getHitFieldValues(new String[]{sortField}, "", true, 0, 10);

            for (Map.Entry obj : HitFieldValues.entrySet()) {
                String Id = String.valueOf(obj.getKey());
                HashMap<String, Object> fieldValueMap = (HashMap<String, Object>) obj.getValue();

                String product = "<h3>" + Id + "</h3>";

                for (Map.Entry item : fieldValueMap.entrySet()) {
                    product += item.getKey() + ": " + String.join(",", (List<String>) (item.getValue()));
                }

                logs.add(product);
            }
            
           
            out.print("<html><body>");
            out.print(String.join("<br>", logs));
            out.print("</body></html>");
            

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } 
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchSortField() throws IOException {

        PrintWriter out = new PrintWriter(System.out);
        try {
            /* TODO output your page here. You may use following sample code. */
            String account = "csharp_unittest"; // your account name
            String password = "csharp_unittest"; // your account password
            boolean isDev = false;
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page
            String sortField = "title"; //sort the search results by this field - IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            boolean sortDesc = true; //sort in an ascending / descending way
            //create search request
            BxRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add a sort field in the provided direction
            bxRequest.addSortField(sortField, sortDesc);

            //set the fields to be returned for each item in the response
            bxRequest.setReturnFields(new ArrayList<String>() {
                {
                    add(sortField);
                }
            });

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //loop on the search response hit ids and print them
            Map<String, Object> HitFieldValues = bxResponse.getHitFieldValues(new String[]{sortField}, "", true, 0, 10);

            for (Map.Entry obj : HitFieldValues.entrySet()) {
                String Id = String.valueOf(obj.getKey());
                HashMap<String, Object> fieldValueMap = (HashMap<String, Object>) obj.getValue();

                String product = "<h3>" + Id + "</h3>";

                for (Map.Entry item : fieldValueMap.entrySet()) {
                    product += item.getKey() + ": " + String.join(",", (List<String>) (item.getValue()));
                }

                logs.add(product);
            }
            
                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        }
    }

}
