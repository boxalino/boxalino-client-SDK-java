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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class SearchAutocompleteItems {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    private String language;
    public boolean print = true;
    private boolean isDev;
    public BxAutocompleteResponse bxAutocompleteResponse = null;
    private HttpContext httpContext = null;
    private String ip="";
    private String referer="";
    private String currentUrl="";    
    private String userAgent = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("unchecked")
    public void SearchAutocompleteItems() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = "boxalino_automated_tests"; // your account name
            password = "boxalino_automated_tests"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            isDev = false; //are the data to be pushed dev or prod data?

            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            //Create HttpContext instance
            httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
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
            logs.add("textual suggestions for \"" + queryText + "\":\n");
            for (String suggestion : bxAutocompleteResponse.getTextualSuggestions()) {

                logs.add("" + suggestion + "");

                logs.add("item suggestions for suggestion \"" + suggestion + "\":\n");
                //loop on the search response hit ids and print them
                for (Map.Entry itemk : bxAutocompleteResponse.getBxSearchResponse(suggestion).getHitFieldValues(fieldNames, "", true, 0, 10).entrySet()) {
                    logs.add("" + itemk.getKey() + "");
                    for (Map.Entry fValueMap : ((Map<String, List<String>>) itemk.getValue()).entrySet()) {
                        logs.add(" - " + fValueMap.getKey() + ": " + String.join(",", ((List<String>) fValueMap.getValue())) + "");
                    }

                }

            }
            logs.add("global item suggestions for \"" + queryText + "\":\n");
            //loop on the search response hit ids and print them
            for (Map.Entry fvalueMap : bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(fieldNames, "", true, 0, 10).entrySet()) {
                String item = String.valueOf(fvalueMap.getKey());
                for (Map.Entry itemInfieldValueMap : ((Map<String, List<String>>) fvalueMap.getValue()).entrySet()) {
                    item += " - " + itemInfieldValueMap.getKey() + ": " + String.join(",", (List<String>) itemInfieldValueMap.getValue()) + "\n";
                }
                logs.add(item);
            }

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }
}
