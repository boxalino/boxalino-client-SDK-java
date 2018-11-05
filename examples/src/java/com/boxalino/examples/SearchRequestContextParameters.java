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
import boxalino.client.SDK.BxRequest;
import boxalino.client.SDK.BxSearchRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class SearchRequestContextParameters {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    private String language;
    private String queryText;
    private int hitCount;
    public boolean print;
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
    @SuppressWarnings("unchecked")
    public void searchRequestContextParameters() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            // required parameters you should set for this example to work
            account = "csharp_unittest"; // your account name
            password = "csharp_unittest"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            boolean isDev = false;

            //Create HttpContext instance
            httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

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
            int index=0;
            for (String item : bxResponse.getHitIds("", true, 0, 10, "id")) {
                logs.add(index + ": returned id " + item);
                 index ++;
            }

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
