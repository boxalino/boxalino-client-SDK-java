/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import Helper.ServletHttpContext;
import boxalino.client.SDK.BxAutocompleteRequest;
import boxalino.client.SDK.BxAutocompleteResponse;
import boxalino.client.SDK.BxClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class SearchAutocompleteItemsBundled {

    public String account;
    public String password;
    String domain;
    List<String> logs;
    String language;
    public boolean print = true;
    boolean isDev;
    public List<BxAutocompleteResponse> bxAutocompleteResponses = null;
    private HttpContext httpContext = null;
    private String ip="";
    private String referer="";
    private String currentUrl="";    
    private String userAgent = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchAutocompleteItemsBundled(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
            logs = new ArrayList<>();
            //optional, just used here in example to collect logs
            print = true;

            //Create HttpContext instance
            httpContext = new ServletHttpContext(domain, request, response);
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
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

                logs.add("textual suggestions for \"" + queryText + "\":\n");
                for (String suggestion : bxAutocompleteResponse.getTextualSuggestions()) {

                    logs.add("" + suggestion + "");

                    logs.add("item suggestions for suggestion  \"" + suggestion + "\" :");

                    // loop on the search response hit ids and print them
                    for (Map.Entry item : bxAutocompleteResponse.getBxSearchResponse(suggestion).getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                        logs.add(item.getKey().toString());
                        for (Map.Entry itemFieldValueMap : ((Map<String, List<String>>) item.getValue()).entrySet()) {
                            logs.add(" - " + itemFieldValueMap.getKey() + ": " + String.join(",", (List<String>) itemFieldValueMap.getValue()) + "");
                        }

                    }

                }
                logs.add("global item suggestions for \"" + queryText + "\":\n");
                // loop on the search response hit ids and print them
                for (Map.Entry hitvalue : bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                    logs.add("" + hitvalue.getKey() + "");
                    for (Map.Entry fvaluemap : ((Map<String, List<String>>) hitvalue.getValue()).entrySet()) {
                        logs.add(" - " + fvaluemap.getKey() + ": " + String.join(",", (List<String>) fvaluemap.getValue()) + "");
                    }

                }
            }
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchAutocompleteItemsBundled() throws IOException {

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
            logs = new ArrayList<>();
            //optional, just used here in example to collect logs
            print = true;

            //Create HttpContext instance
            httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
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

                logs.add("textual suggestions for \"" + queryText + "\":\n");
                for (String suggestion : bxAutocompleteResponse.getTextualSuggestions()) {

                    logs.add("" + suggestion + "");

                    logs.add("item suggestions for suggestion  \"" + suggestion + "\" :");

                    // loop on the search response hit ids and print them
                    for (Map.Entry item : bxAutocompleteResponse.getBxSearchResponse(suggestion).getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                        logs.add(item.getKey().toString());
                        for (Map.Entry itemFieldValueMap : ((Map<String, List<String>>) item.getValue()).entrySet()) {
                            logs.add(" - " + itemFieldValueMap.getKey() + ": " + String.join(",", (List<String>) itemFieldValueMap.getValue()) + "");
                        }

                    }

                }
                logs.add("global item suggestions for \"" + queryText + "\":\n");
                // loop on the search response hit ids and print them
                for (Map.Entry hitvalue : bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                    logs.add(hitvalue.getKey() + "");
                    for (Map.Entry fvaluemap : ((Map<String, List<String>>) hitvalue.getValue()).entrySet()) {
                        logs.add(" - " + fvaluemap.getKey() + ": " + String.join(",", (List<String>) fvaluemap.getValue()) + "");
                    }

                }
            }
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }
}
