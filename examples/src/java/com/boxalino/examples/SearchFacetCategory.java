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
import boxalino.client.SDK.BxFacets;
import boxalino.client.SDK.BxRequest;
import boxalino.client.SDK.BxSearchRequest;
import com.boxalino.p13n.api.thrift.FacetValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class SearchFacetCategory {

    public String account;
    public String password;
    public boolean print;
    public BxChooseResponse bxResponse = null;
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
    public void searchFacetCategory() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = "csharp_unittest"; // your account name
            password = "csharp_unittest"; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)            
            boolean isDev = false; //are the data to be pushed dev or prod data?            
            List<String> logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;

            //Create HttpContext instance
            httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);
            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page
            String selectedValue = null;// pass here the selected value

            //create search request
            BxRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add a facert
            BxFacets facets = new BxFacets();
            facets.addCategoryFacet(selectedValue, 2);
            bxRequest.setFacets(facets);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //get the facet responses
            facets = bxResponse.getFacets("", true, 0, 10);

            //show the category breadcrumbs
            int level = 0;
            logs.add("home");

            for (Map.Entry item : facets.getParentCategories().entrySet()) {

                logs.add("\n" + item.getValue() + "");
                level++;
            }
            logs.add(" ");
            //show the category facet values
            for (Map.Entry value : facets.getCategories().entrySet()) {
                logs.add("" + facets.getCategoryValueLabel((FacetValue) value.getValue()) + " (" + facets.getCategoryValueCount((FacetValue) value.getValue()) + ")");

            }
            logs.add(" ");
            //loop on the search response hit ids and print them
            int index=0;
            for (String item : bxResponse.getHitIds("", true, 0, 10, "id")) {
                logs.add(index + ": returned id " + item + "");
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
