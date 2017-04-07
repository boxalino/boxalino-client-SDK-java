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
public class SearchRequestContextParameters {

    public String account;
    public String password;
    String domain;
    List<String> logs;
    String language;
    String queryText;
    int hitCount;
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
    public void searchRequestContextParameters(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            // required parameters you should set for this example to work
            String account = "csharp_unittest"; // your account name
            String password = "csharp_unittest"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            boolean isDev = false;

            /* TODO Instantiate ServletHttpContext to manage cookies.*/
            ServletHttpContext.request = request;
            ServletHttpContext.response = response;
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page

            Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
            requestParameters.put("geoIP-latitude", new ArrayList<String>() {
                {
                    add("47.36");
                }
            });
            requestParameters.put("geoIP-longitude", new ArrayList<String>() {
                {
                    add("6.1517993");
                }
            });
            //create search request
            BxRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");
            //set the fields to be returned for each item in the response

            for (Map.Entry item : requestParameters.entrySet()) {
                bxClient.addRequestContextParameter(String.valueOf(item.getKey()), (ArrayList<String>) item.getValue());
            }

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //indicate the search made with the number of results found
            logs.add("Results for query \"" + queryText + "\" (" + bxResponse.getTotalHitCount("", true, 0, 10) + "):<br>");

            //loop on the search response hit ids and print them
            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
            }

            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

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
    public void searchRequestContextParameters() throws IOException {

        PrintWriter out = new PrintWriter(System.out);
        try {
            /* TODO output your page here. You may use following sample code. */
            // required parameters you should set for this example to work
            String account = "csharp_unittest"; // your account name
            String password = "csharp_unittest"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            boolean isDev = false;

            
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page

            Map<String, List<String>> requestParameters = new HashMap<String, List<String>>();
            requestParameters.put("geoIP-latitude", new ArrayList<String>() {
                {
                    add("47.36");
                }
            });
            requestParameters.put("geoIP-longitude", new ArrayList<String>() {
                {
                    add("6.1517993");
                }
            });
            //create search request
            BxRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");
            //set the fields to be returned for each item in the response

            for (Map.Entry item : requestParameters.entrySet()) {
                bxClient.addRequestContextParameter(String.valueOf(item.getKey()), (ArrayList<String>) item.getValue());
            }

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //indicate the search made with the number of results found
            logs.add("Results for query \"" + queryText + "\" (" + bxResponse.getTotalHitCount("", true, 0, 10) + "):<br>");

            //loop on the search response hit ids and print them
            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
            }

            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } 
    }

}
