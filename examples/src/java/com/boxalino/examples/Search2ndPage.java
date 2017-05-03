/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import boxalino.client.SDK.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Hashir Faizy
 */
public class Search2ndPage {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    public boolean print = true;
    boolean isDev;
    public BxChooseResponse bxResponse = null;
    private HttpContext httpContext = null;
    private String ip = "";
    private String referer = "";
    private String currentUrl = "";
    private String userAgent = "";

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
            print = true;
            //Create HttpContext instance
            httpContext = new HttpContext(domain, userAgent, ip, referer, currentUrl);
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

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
            int index = 0;
            for (String item : bxResponse.getHitIds("", true, 0, 10, "id")) {
                logs.add(index + ": returned id " + item);
                index++;
            }

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
