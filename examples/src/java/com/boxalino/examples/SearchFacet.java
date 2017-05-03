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
public class SearchFacet {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    private String language;
    public boolean print = true;
    boolean isDev;
    public BxFacets facets = null;
    public List<String> facetField = null;
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
    @SuppressWarnings("unchecked")
    public void searchFacet() throws IOException {

        try {
            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and c# Thrift Client files
            //required parameters you should set for this example to work
            account = "boxalino_automated_tests"; // your account name
            password = "boxalino_automated_tests"; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            isDev = false; //are the data to be pushed dev or prod data?
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;

            //Create HttpContext instance
            httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page
            facetField = new ArrayList<String>() {
                {
                    add("products_color");
                }
            }; //the field to consider in the filter - IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            String selectedValue = null;// pass here the selected value
            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set the fields to be returned for each item in the response
            bxRequest.setReturnFields((ArrayList<String>) (facetField));

            //add a facert
            facets = new BxFacets();
            facets.addFacet(facetField.get(0), selectedValue, "string", "", 2, false);
            bxRequest.setFacets(facets);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //get the facet responses
            facets = bxResponse.getFacets("", true, 0, 10);

            //loop on the search response hit ids and print them
            for (Map.Entry fieldValue : facets.getFacetValues(facetField.get(0)).entrySet()) {
                logs.add("" + facets.getFacetValueLabel(facetField.get(0), (FacetValue) fieldValue.getValue()) + " (" + facets.getFacetValueCount(facetField.get(0), (FacetValue) fieldValue.getValue()) + ")");
                if ((facets.isFacetValueSelected(facetField.get(0), (FacetValue) fieldValue.getValue())).isEmpty()) {
                    logs.add("[X]");
                }
            }
            //loop on the search response hit ids and print them
            for (Map.Entry item : bxResponse.getHitFieldValues(facetField, "", true, 0, 10).entrySet()) {
                logs.add("" + item.getKey() + "");
                for (Map.Entry itemField : ((Map<String, List<String>>) item.getValue()).entrySet()) {
                    logs.add(itemField.getKey() + ": " + String.join(",", (List<String>) itemField.getValue()));
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
