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
public class SearchReturnFields {

    public String account;
    public String password;
    private String domain;
    private String language;
    private List<String> logs;
    private String queryText;
    private int hitCount;
    public boolean print;
    private ArrayList<String> fieldNames;
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
    public void searchReturnFields() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            account = "boxalino_automated_tests"; // your account name
            password = "boxalino_automated_tests"; // your account password
            domain = "";// your web-site domain (e.g.: www.abc.com)
            language = "en";// a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women";// a search query
            hitCount = 10;//a maximum number of search result to return in one page
            fieldNames = new ArrayList<>();
            logs = new ArrayList<>();//optional, just used here in example to collect logs
            fieldNames.add("products_color"); //IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            print = true;
            boolean isDev = false;

            //Create HttpContext instance
            httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

            //create search request
            BxSearchRequest bxrequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set the fields to be returned for each item in the response
            bxrequest.setReturnFields(fieldNames);

            //add the request
            bxClient.addRequest(bxrequest);
            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            for (Map.Entry obj : bxResponse.getHitFieldValues(fieldNames, "", true, 0, 10).entrySet()) {
                String Id = String.valueOf(obj.getKey());
                HashMap<String, Object> fieldValueMap = (HashMap<String, Object>) obj.getValue();

                String entity = "" + Id + "";

                for (Map.Entry item : ((HashMap<String, Object>) fieldValueMap).entrySet()) {
                    entity += item.getKey() + ": " + String.join(",", (List<String>) (item.getValue()));
                }
                logs.add(entity);
            }

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
