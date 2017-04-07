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
import boxalino.client.SDK.BxRecommendationRequest;
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
public class RecommendationsSimilar {

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
    public void recommendationsSimilar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        try {

            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */
            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = "boxalino_automated_tests";// your account name
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
            String choiceId = "similar"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            String itemFieldId = "id"; // the field you want to use to define the id of the product (normally id, but could also be a group id if you have a difference between group id and sku) 
            String itemFieldIdValue = "1940"; //the product id the user is currently looking at
            int hitCount = 10; //a maximum number of recommended result to return in one page

            //create similar recommendations request
            BxRecommendationRequest bxRequest = new BxRecommendationRequest(language, choiceId, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequest.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
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
    public void recommendationsSimilar() throws IOException {

        PrintWriter out = new PrintWriter(System.out);
        try {

            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */
            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = "boxalino_automated_tests";// your account name
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
            String choiceId = "similar"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            String itemFieldId = "id"; // the field you want to use to define the id of the product (normally id, but could also be a group id if you have a difference between group id and sku) 
            String itemFieldIdValue = "1940"; //the product id the user is currently looking at
            int hitCount = 10; //a maximum number of recommended result to return in one page

            //create similar recommendations request
            BxRecommendationRequest bxRequest = new BxRecommendationRequest(language, choiceId, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequest.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            for (Map.Entry item : bxResponse.getHitIds("", true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
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
