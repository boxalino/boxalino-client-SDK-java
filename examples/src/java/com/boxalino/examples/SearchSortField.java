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
public class SearchSortField {

    public String account;
    public String password;
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
    public void searchSortField() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            account = "csharp_unittest"; // your account name
            password = "csharp_unittest"; // your account password
            boolean isDev = false;
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            List<String> logs = new ArrayList<>(); //optional, just used here in example to collect logs

            //Create HttpContext instance
            httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

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
            Map<String, Object> HitFieldValues = bxResponse.getHitFieldValues(java.util.Arrays.asList(sortField), "", true, 0, 10);

            for (Map.Entry obj : HitFieldValues.entrySet()) {
                String Id = String.valueOf(obj.getKey());
                HashMap<String, Object> fieldValueMap = (HashMap<String, Object>) obj.getValue();

                String product = "" + Id + "";

                for (Map.Entry item : fieldValueMap.entrySet()) {
                    product += item.getKey() + ": " + String.join(",", (List<String>) (item.getValue()));
                }

                logs.add(product);
            }

            System.out.println(String.join("\n", logs));

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
