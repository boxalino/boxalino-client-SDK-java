/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import Helper.ServletHttpContext;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxRecommendationRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class RecommendationsSimilarComplementary {

    public String account;
    public String password;
    private String domain;
    private List<String> logs;
    private String language;
    public boolean print = true;
    private boolean isDev;
    public BxChooseResponse bxResponse = null;
    private HttpContext httpContext = null;
    private String ip="";
    private String referer="";
    private String currentUrl="";    
    private String userAgent = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws IOException if an I/O error occurs
     */
    public void recommendationsSimilarComplementary(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
            isDev = false; //are the data to be pushed dev or prod data?
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            language = "en";
            //Create HttpContext instance
            httpContext = new ServletHttpContext(domain, request, response);
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);

            String choiceIdSimilar = "similar"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            String choiceIdComplementary = "complementary";
            String itemFieldId = "id"; // the field you want to use to define the id of the product (normally id, but could also be a group id if you have a difference between group id and sku)
            String itemFieldIdValue = "1940"; //the product id the user is currently looking at
            int hitCount = 10; //a maximum number of recommended result to return in one page

            //create similar recommendations request
            BxRecommendationRequest bxRequestSimilar = new BxRecommendationRequest(language, choiceIdSimilar, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequestSimilar.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequestSimilar);

            //create complementary recommendations request
            BxRecommendationRequest bxRequestComplementary = new BxRecommendationRequest(language, choiceIdComplementary, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequestComplementary.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequestComplementary);

            //make the query to Boxalino server and get back the response for all requests (make sure you have added all your requests before calling getResponse; i.e.: do not push the first request, then call getResponse, then add a new request, then call getResponse again it wil not work; N.B.: if you need to do to separate requests call, then you cannot reuse the same instance of BxClient, but need to create a new one)
            bxResponse = bxClient.getResponse();

            //loop on the recommended response hit ids and print them
            logs.add("recommendations of similar items:");
            for (Map.Entry item : bxResponse.getHitIds(choiceIdSimilar, true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
            }
            logs.add("");
            //retrieve the recommended responses object of the complementary request
            logs.add("recommendations of complementary items:");
            //loop on the recommended response hit ids and print them
            for (Map.Entry itemHitId : bxResponse.getHitIds(choiceIdComplementary, true, 0, 10, "id").entrySet()) {
                logs.add(itemHitId.getKey() + ": returned id " + itemHitId.getValue());
            }
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Use this method if do not want to manage cookies
     *
     * @throws IOException if an I/O error occurs
     */
    public void recommendationsSimilarComplementary() throws IOException {

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
            isDev = false; //are the data to be pushed dev or prod data?
            logs = new ArrayList<>(); //optional, just used here in example to collect logs
            print = true;
            //Create HttpContext instance
            httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext);
            String choiceIdSimilar = "similar"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            String choiceIdComplementary = "complementary";
            String itemFieldId = "id"; // the field you want to use to define the id of the product (normally id, but could also be a group id if you have a difference between group id and sku)
            String itemFieldIdValue = "1940"; //the product id the user is currently looking at
            int hitCount = 10; //a maximum number of recommended result to return in one page
            language = "en";
            //create similar recommendations request
            BxRecommendationRequest bxRequestSimilar = new BxRecommendationRequest(language, choiceIdSimilar, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequestSimilar.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequestSimilar);

            //create complementary recommendations request
            BxRecommendationRequest bxRequestComplementary = new BxRecommendationRequest(language, choiceIdComplementary, hitCount);

            //indicate the product the user is looking at now (reference of what the recommendations need to be similar to)
            bxRequestComplementary.setProductContext(itemFieldId, itemFieldIdValue, "");

            //add the request
            bxClient.addRequest(bxRequestComplementary);

            //make the query to Boxalino server and get back the response for all requests (make sure you have added all your requests before calling getResponse; i.e.: do not push the first request, then call getResponse, then add a new request, then call getResponse again it wil not work; N.B.: if you need to do to separate requests call, then you cannot reuse the same instance of BxClient, but need to create a new one)
            bxResponse = bxClient.getResponse();

            //loop on the recommended response hit ids and print them
            logs.add("recommendations of similar items:");
            for (Map.Entry item : bxResponse.getHitIds(choiceIdSimilar, true, 0, 10, "id").entrySet()) {
                logs.add(item.getKey() + ": returned id " + item.getValue());
            }
            logs.add("");
            //retrieve the recommended responses object of the complementary request
            logs.add("recommendations of complementary items:");
            //loop on the recommended response hit ids and print them
            for (Map.Entry itemHitId : bxResponse.getHitIds(choiceIdComplementary, true, 0, 10, "id").entrySet()) {
                logs.add(itemHitId.getKey() + ": returned id " + itemHitId.getValue());
            }
            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
