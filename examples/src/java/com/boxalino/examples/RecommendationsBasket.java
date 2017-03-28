/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.CustomBasketContent;
import Helper.HttpContext;
import boxalino.client.SDK.BxChooseResponse;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxRecommendationRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;

/**
 *
 * @author HASHIR
 */
public class RecommendationsBasket {

    public String _account;
    public String _password;
    String domain;
    List<String> logs;
    String language;
    public boolean _print;
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
    protected void recommendationsBasket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();
        try {

            String account = this._account; // your account name
            String password = this._password; // your account password
            domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>();
            //optional, just used here in example to collect logs
            boolean print = this._print;
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);
            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            String choiceId = "basket"; //the recommendation choice id (standard choice ids are: "similar" => similar products on product detail page, "complementary" => complementary products on product detail page, "basket" => cross-selling recommendations on basket page, "search"=>search results, "home" => home page personalized suggestions, "category" => category page suggestions, "navigation" => navigation product listing pages suggestions)
            String itemFieldId = "id"; // the field you want to use to define the id of the product (normally id, but could also be a group id if you have a difference between group id and sku)

            ArrayList<CustomBasketContent> itemFieldIdValuesPrices = new ArrayList<CustomBasketContent>(); //the product ids and their prices that the user currently has in his basket
            CustomBasketContent customBasketContent = new CustomBasketContent();
            customBasketContent.Id = "1940";
            customBasketContent.Price = "10.80";
            itemFieldIdValuesPrices.add(customBasketContent);
            customBasketContent = new CustomBasketContent();
            customBasketContent.Id = "1234";
            customBasketContent.Price = "130.5";
            itemFieldIdValuesPrices.add(customBasketContent);

            int hitCount = 10; //a maximum number of recommended result to return in one page

            //create similar recommendations request
            BxRecommendationRequest bxRequest = new BxRecommendationRequest(language, choiceId, hitCount);

            //indicate the products the user currently has in his basket (reference of products for the recommendations)
            bxRequest.setBasketProductWithPrices(itemFieldId, itemFieldIdValuesPrices, "", "");

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
        } catch (TException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (URISyntaxException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (NoSuchFieldException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        } catch (IOException ex) {

            out.print("<html><body>");
            out.print(ex.getMessage());
            out.print("</body></html>");
        }
    }

}
