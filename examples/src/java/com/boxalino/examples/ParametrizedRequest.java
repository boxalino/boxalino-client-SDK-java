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
import boxalino.client.SDK.BxFilter;
import boxalino.client.SDK.BxParametrizedRequest;
import boxalino.client.SDK.BxSortFields;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class ParametrizedRequest {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;    
    boolean print = true;
    boolean isDev;
    public BxChooseResponse bxResponse = null;
    private String requestWeightedParametersPrefix;
    private String requestFiltersPrefix;
    private String requestFacetsPrefix;
    private String requestSortFieldPrefix;
    private String requestReturnFieldsName;
    private List<String> bxReturnFields;
    private String getItemFieldsCB;
    private String language;
    private String choiceId;
    private int hitCount;
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
    public void parametrizedRequest() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            //required parameters you should set for this example to work
            account = "csharp_unittest";
            password = "csharp_unittest";
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            isDev = true;
            //Create HttpContext instance
            httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);
            bxClient.setRequestMap(new HashMap<>());

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            choiceId = "productfinder"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            hitCount = 10; //a maximum number of recommended result to return in one page
            requestWeightedParametersPrefix = "bxrpw_";
            requestFiltersPrefix = "bxfi_";
            requestFacetsPrefix = "bxfa_";
            requestSortFieldPrefix = "bxsf_";
            requestReturnFieldsName = "bxrf";

            bxReturnFields = new ArrayList<String>();
            bxReturnFields.add("id");  //the list of fields which should be returned directly by Boxalino, the others will be retrieved through a call-back function
            getItemFieldsCB = "getItemFieldsCB";

            //create the request and set the parameter prefix values
            BxParametrizedRequest bxRequest = new BxParametrizedRequest(language, choiceId, hitCount, 0, bxReturnFields, getItemFieldsCB);
            bxRequest.setRequestWeightedParametersPrefix(requestWeightedParametersPrefix);
            bxRequest.setRequestFiltersPrefix(requestFiltersPrefix);
            bxRequest.setRequestFacetsPrefix(requestFacetsPrefix);
            bxRequest.setRequestSortFieldPrefix(requestSortFieldPrefix);

            bxRequest.setRequestReturnFieldsName(requestReturnFieldsName);

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            BxChooseResponse bxResponse = bxClient.getResponse();

            logs.add("weighted parameters");
            for (Map.Entry<String, Object> item : bxRequest.getWeightedParameters().entrySet()) {
                for (Map.Entry<String, Object> fieldItems : ((HashMap<String, Object>) item.getValue()).entrySet()) {
                    logs.add(item.getKey() + ": " + fieldItems.getKey() + ": " + fieldItems.getValue());
                }
            }

            logs.add("..");
            logs.add("filters");

            for (Map.Entry<String, BxFilter> bxFilter : bxRequest.getFilters().entrySet()) {
                logs.add(((BxFilter) bxFilter.getValue()).getFieldName() + ": " + String.join(",", bxFilter.getValue().getValues()) + " :" + bxFilter.getValue().isNegative());
            }
            logs.add("..");
            logs.add("facets");
            BxFacets bxFacets = bxRequest.getFacets();

            for (String fieldName : bxFacets.getFieldNames()) {
                logs.add(fieldName + ":" + String.join(",", bxFacets.getSelectedValues(fieldName).values().toArray(new CharSequence[bxFacets.getSelectedValues(fieldName).size()])));
            }
            logs.add("..");
            logs.add("sort fields");

            BxSortFields bxSortFields = bxRequest.getSortFields();

            for (Map.Entry<String, Boolean> fieldName : bxSortFields.getSortFields().entrySet()) {
                logs.add(fieldName + ": " + bxSortFields.isFieldReverse(fieldName.getValue().toString()));
            }
            logs.add("..");
            logs.add("results");
            logs.add(bxResponse.toJson(bxRequest.getAllReturnFields()));

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
