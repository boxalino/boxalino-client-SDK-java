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
import java.util.List;

/**
 *
 * @author HASHIR
 */
public class SearchCorrected {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    private String language;
    public boolean print = true;
    private boolean isDev;
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
    public void searchCorrected() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
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
            httpContext = new HttpContext(domain, userAgent, ip, referer, currentUrl);
            //Create the Boxalino Client SDK instance
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null);

            String language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //if the query is corrected, then print the corrrect query text
            if (bxResponse.areResultsCorrected("", 0, 10)) {
                logs.add("Corrected query \"" + queryText + "\" into \"" + bxResponse.getCorrectedQuery("", 0) + "\"");
            }

            //loop on the search response hit ids and print them
            int index=0;
            for (String item : bxResponse.getHitIds("", true, 0, 10, "id")) {
                logs.add("" + index + ": returned id " + item + "");
                index++;
            }

            if (bxResponse.getHitIds("", true, 0, 10, "id").size() == 0) {
                logs.add("There are no corrected results. This might be normal, but it also might mean that the first execution of the corpus preparation was not done and published yet. Please refer to the example backend_data_init and make sure you have done the following steps at least once: 1) publish your data 2) run the prepareCorpus case 3) publish your data again");
            }

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }
}
