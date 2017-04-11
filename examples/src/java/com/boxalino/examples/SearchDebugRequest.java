/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxSearchRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class SearchDebugRequest {

    public String account;
    public String password;
    String domain;
    List<String> logs;
    String language;
    public boolean print = true;
    boolean isDev;
    public BxChooseResponse bxResponse = null;
    public BxClient bxClient = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     
     * @throws IOException if an I/O error occurs
     */
    public void searchDebugRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>();

            //optional, just used here in example to collect logs
            boolean print = true;

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            /* TODO Instantiate Request & Response to manage cookies.*/
            bxClient.request = request;
            bxClient.response = response;
            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void searchDebugRequest() throws IOException {

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
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>();

            //optional, just used here in example to collect logs
            boolean print = true;

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String queryText = "women"; // a search query
            int hitCount = 10; //a maximum number of search result to return in one page

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }
}
