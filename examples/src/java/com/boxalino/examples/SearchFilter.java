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
import boxalino.client.SDK.BxFilter;
import boxalino.client.SDK.BxSearchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class SearchFilter {

    public String account;
    public String password;
    String domain;
    ArrayList<String> logs;
    String language;
    String queryText;
    int hitCount;
    public boolean print;
    String filterField;
    List<String> filterValues;
    boolean filterNegative;

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
    protected void searchFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */

            /**
             * In this example, we make a simple search query, add a filter and
             * get the search results and print their ids Filters are different
             * than facets because they are not returned to the user and should
             * not be related to a user interaction Filters should be "system"
             * filters (e.g.: filter on a category within a category page,
             * filter on product which are visible and not out of stock, etc.)
             */
            //required parameters you should set for this example to work
            String account = "boxalino_automated_tests"; // your account name
            String password = "boxalino_automated_tests"; // your account password

            domain = ""; // your web-site domain (e.g.: www.abc.com)
            logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;
            boolean isDev = false;
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            language = "en"; // a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women"; // a search query
            hitCount = 10; //a maximum number of search result to return in one page
            filterField = "id"; //the field to consider in the filter
            filterValues = new ArrayList<String>() {
                {
                    add("41");
                    add("1940");
                }
            }; //the field to consider any of the values should match (or not match)
            filterNegative = true; //false by default, should the filter match the values or not?

            //create search request
            BxSearchRequest bxRequest = new BxSearchRequest(language, queryText, hitCount, "");
            //add a filter
            bxRequest.addFilter(new BxFilter(filterField, (ArrayList<String>) filterValues, filterNegative));

            //add the request
            bxClient.addRequest(bxRequest);

            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            //loop on the search response hit ids and print them
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
