/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.ServletHttpContext;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxSearchRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class SearchCorrected {

    String account;
    String password;
    String domain;
    List<String> logs;
    String language;
    boolean print = true;
    boolean isDev;
    public BxChooseResponse bxResponse = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void searchCorrected(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

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
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            /* TODO Instantiate ServletHttpContext to manage cookies.*/
            ServletHttpContext.request = request;
            ServletHttpContext.response = response;

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

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
            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add("" + item.getKey() + ": returned id " + item.getValue() + "");
            }

            if (bxResponse.getHitIds("", true, 0, 10, "id").size() == 0) {
                logs.add("There are no corrected results. This might be normal, but it also might mean that the first execution of the corpus preparation was not done and published yet. Please refer to the example backend_data_init and make sure you have done the following steps at least once: 1) publish your data 2) run the prepareCorpus case 3) publish your data again");
            }

            if (print) {
                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } 
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchCorrected() throws IOException {

        PrintWriter out = new PrintWriter(System.out);

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
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

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
            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add("" + item.getKey() + ": returned id " + item.getValue() + "");
            }

            if (bxResponse.getHitIds("", true, 0, 10, "id").size() == 0) {
                logs.add("There are no corrected results. This might be normal, but it also might mean that the first execution of the corpus preparation was not done and published yet. Please refer to the example backend_data_init and make sure you have done the following steps at least once: 1) publish your data 2) run the prepareCorpus case 3) publish your data again");
            }

            if (print) {
                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } 
    }
}
