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
import com.boxalino.p13n.api.thrift.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class SearchSubPhrases {

    public String account;
    public String password;
    String domain;
    String language;
    List<String> logs;
    String queryText;
    int hitCount;
    public boolean print;
    public BxChooseResponse bxResponse = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response     
     * @throws IOException if an I/O error occurs
     */
    public void searchSubPhrases(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */

            account = "csharp_unittest";
            password = "csharp_unittest";
            domain = "";// your web-site domain (e.g.: www.abc.com)
            language = "en";// a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women pack";// a search query
            hitCount = 10;//a maximum number of search result to return in one page
            logs = new ArrayList<String>();//optional, just used here in example to collect logs
            boolean print = true;
            boolean isDev = false;

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);
            /* TODO Instantiate Request & Response to manage cookies.*/
            bxClient.request = request;
            bxClient.response = response;
            //create search request
            BxSearchRequest bxrequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add the request
            bxClient.addRequest(bxrequest);
            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //check if the system has generated sub phrases results
            if (bxResponse.areThereSubPhrases("", 0)) {

                logs.add("No results found for all words in " + queryText + ",but following partial matches were found:<br\\>");

                for (Map.Entry subPhrase : bxResponse.getSubPhrasesQueries("", 0).entrySet()) {

                    logs.add("Results for \"" + ((SearchResult) subPhrase.getValue()).queryText + "\" (" + bxResponse.getSubPhraseTotalHitCount(((SearchResult) subPhrase.getValue()).queryText, "", 0) + " hits):");
                    //loop on the search response hit ids and print them

                    for (Map.Entry id : bxResponse.getSubPhraseHitIds(((SearchResult) subPhrase.getValue()).queryText, "", 0, "id").entrySet()) {

                        logs.add(id.getKey().toString() + ": returned id " + id.getValue());
                    }
                    logs.add("");
                }
            } else {

                for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {

                    logs.add(item.getKey() + "i: returned id " + item.getValue());
                }

            }

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
    public void searchSubPhrases() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */

            account = "csharp_unittest";
            password = "csharp_unittest";
            domain = "";// your web-site domain (e.g.: www.abc.com)
            language = "en";// a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women pack";// a search query
            hitCount = 10;//a maximum number of search result to return in one page
            logs = new ArrayList<String>();//optional, just used here in example to collect logs
            boolean print = true;
            boolean isDev = false;

            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            //create search request
            BxSearchRequest bxrequest = new BxSearchRequest(language, queryText, hitCount, "");

            //add the request
            bxClient.addRequest(bxrequest);
            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //check if the system has generated sub phrases results
            if (bxResponse.areThereSubPhrases("", 0)) {

                logs.add("No results found for all words in " + queryText + ",but following partial matches were found:<br\\>");

                for (Map.Entry subPhrase : bxResponse.getSubPhrasesQueries("", 0).entrySet()) {

                    logs.add("Results for \"" + ((SearchResult) subPhrase.getValue()).queryText + "\" (" + bxResponse.getSubPhraseTotalHitCount(((SearchResult) subPhrase.getValue()).queryText, "", 0) + " hits):");
                    //loop on the search response hit ids and print them

                    for (Map.Entry id : bxResponse.getSubPhraseHitIds(((SearchResult) subPhrase.getValue()).queryText, "", 0, "id").entrySet()) {

                        logs.add(id.getKey().toString() + ": returned id " + id.getValue());
                    }
                    logs.add("");
                }
            } else {

                for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {

                    logs.add(item.getKey() + "i: returned id " + item.getValue());
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
