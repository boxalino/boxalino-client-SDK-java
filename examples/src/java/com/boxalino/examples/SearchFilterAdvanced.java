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
import boxalino.client.SDK.BxFilter;
import boxalino.client.SDK.BxSearchRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class SearchFilterAdvanced {

    public String account;
    public String password;
    String domain;
    List<String> logs;
    String language;
    String queryText;
    int hitCount;
    public boolean print;
    String filterField;
    List<String> filterValues;
    boolean filterNegative;
    String filterField2;
    List<String> filterValues2;
    boolean filterNegative2;
    boolean orFilters;
    List<String> fieldNames;
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
    public void searchFilterAdvanced() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            // required parameters you should set for this example to work
            account = "boxalino_automated_tests"; // your account name
            password = "boxalino_automated_tests"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            boolean isDev = false;

            //Create HttpContext instance
            httpContext = new HttpContext(domain, userAgent, ip, referer, currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page
            filterField = "id"; //the field to consider in the filter
            filterValues = new ArrayList<String>() {
                {
                    add("41");
                    add("1941");
                }
            }; //the field to consider any of the values should match (or not match)
            filterNegative = true; //false by default, should the filter match the values or not?
            filterField2 = "products_color"; //the field to consider in the filter
            filterValues2 = new ArrayList<String>() {
                {
                    add("Yellow");
                }
            }; //the field to consider any of the values should match (or not match)
            filterNegative2 = false; //false by default, should the filter match the values or not?
            orFilters = true; //the two filters are either or (only one of them needs to be correct
            fieldNames = new ArrayList<String>() {
                {
                    add("products_color");
                }
            }; //IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set the fields to be returned for each item in the response
            bxRequest.setReturnFields((ArrayList<String>) fieldNames);

            //add a filter
            bxRequest.addFilter(new BxFilter(filterField, (ArrayList<String>) filterValues, filterNegative));
            bxRequest.addFilter(new BxFilter(filterField2, (ArrayList<String>) filterValues2, filterNegative2));
            bxRequest.setOrFilters(orFilters);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //loop on the search response hit ids and print them
            for (Map.Entry item : bxResponse.getHitFieldValues(fieldNames, "", true, 0, 10).entrySet()) {
                logs.add("" + item.getKey() + "");
                for (Map.Entry val : ((Map<String, List<String>>) item.getValue()).entrySet()) {
                    logs.add(val.getKey() + ": " + String.join(",", (List<String>) (val.getValue())));
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
