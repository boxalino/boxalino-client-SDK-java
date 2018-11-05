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

/**
 *
 * @author HASHIR
 */
public class SearchFilter {

    public String account;
    public String password;
    private String domain;
    private ArrayList<String> logs;
    private String language;
    private String queryText;
    private int hitCount;
    public boolean print;
    private String filterField;
    private List<String> filterValues;
    private boolean filterNegative;
    private HttpContext httpContext = null;
    private String ip = "";
    private String referer = "";
    private String currentUrl = "";
    private String userAgent = "";

    public BxChooseResponse bxResponse = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchFilter() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */

            /**
             * In this example, we make a simple search query, add a filter and
             * get the search results and print their ids Filters are different
             * than facets because they are not returned to the user and should
             * not be related to a user interaction Filters should be "system"
             * filters (e.g.: filter on a category within a category page,
             * filter on product which are visible and not out of stock, etc.)
             */
            //required parameters you should set for this example to work
            account = "boxalino_automated_tests"; // your account name
            password = "boxalino_automated_tests"; // your account password

            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            boolean isDev = false;

            //Create HttpContext instance
            httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page
            filterField = "id"; //the field to consider in the filter
            filterValues = new ArrayList<String>() {
                {
                    add("41");
                    add("1940");
                }
            }; //the field to consider any of the values should match (or not match)
            filterNegative = true; //false by default, should the filter match the values or not?

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");
            //add a filter
            bxRequest.addFilter(new BxFilter(filterField, (ArrayList<String>) filterValues, filterNegative));

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //loop on the search response hit ids and print them
            int index=0;
            for (String item : bxResponse.getHitIds("", true, 0, 10, "id")) {
                logs.add(item + ": returned id " + item);
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
