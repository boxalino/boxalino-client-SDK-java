/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import boxalino.client.SDK.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import Helper.*;

/**
 *
 * @author Hashir Faizy
 */
public class Search2ndPage {

    public String account;
    public String password;
    String domain;
    List<String> logs;
    String[] languages;
    public boolean print = true;
    boolean isDev;
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
    public void Search2ndPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        try {
            /* TODO output your page here. You may use following sample code. */

            //required parameters you should set for this example to work
            account = "csharp_unittest";
            password = "csharp_unittest";
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            isDev = true;
            languages = new String[]{"en"};
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            print = true;
            /* TODO Instantiate ServletHttpContext to manage cookies.*/
            ServletHttpContext.request = request;
            ServletHttpContext.response = response;
            
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);
            
            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "watch"; // a search query
            int hitCount = 5; //a maximum number of search result to return in one page
            int offset = 5; //the offset to start the page with (if = hitcount ==> page 2)

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set an offset for the returned search results (start at position provided)
            bxRequest.setOffset(offset);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

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
    public void Search2ndPage() throws IOException {

        PrintWriter out = new PrintWriter(System.out);

        try {
            /* TODO output your page here. You may use following sample code. */

            //required parameters you should set for this example to work
            account = "csharp_unittest";
            password = "csharp_unittest";
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            isDev = true;
            languages = new String[]{"en"};
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            print = true;
           
            
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);
            
            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "watch"; // a search query
            int hitCount = 5; //a maximum number of search result to return in one page
            int offset = 5; //the offset to start the page with (if = hitcount ==> page 2)

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set an offset for the returned search results (start at position provided)
            bxRequest.setOffset(offset);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

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
