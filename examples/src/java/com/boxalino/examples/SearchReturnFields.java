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
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
public class SearchReturnFields {

    public String account;
    public String password;
    String domain;
    String language;
    List<String> logs;
    String queryText;
    int hitCount;
    public boolean print;
    ArrayList<String> fieldNames;
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
    protected void searchReturnFields(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();

        try {
            /* TODO output your page here. You may use following sample code. */
            String account = "boxalino_automated_tests"; // your account name
            String password = "boxalino_automated_tests"; // your account password

            domain = "";// your web-site domain (e.g.: www.abc.com)
            language = "en";// a valid language code (e.g.: "en", "fr", "de", "it", ...)
            queryText = "women";// a search query
            hitCount = 10;//a maximum number of search result to return in one page
            fieldNames = new ArrayList<String>();
            logs = new ArrayList<String>();//optional, just used here in example to collect logs
            fieldNames.add("products_color"); //IMPORTANT: you need to put "products_" as a prefix to your field name except for standard fields: "title", "body", "discountedPrice", "standardPrice"
            boolean print = true;
            boolean isDev = false;
            //Create the Boxalino Client SDK instance
            //N.B.: you should not create several instances of BxClient on the same page, make sure to save it in a static variable and to re-use it.
            BxClient bxClient = new BxClient(account, password, domain, isDev, null, 0, null, null, null, null);

            //create search request
            BxSearchRequest bxrequest = new BxSearchRequest(language, queryText, hitCount, "");

            //set the fields to be returned for each item in the response
            bxrequest.setReturnFields(fieldNames);

            //add the request
            bxClient.addRequest(bxrequest);
            //make the query to Boxalino server and get back the response for all requests
            bxResponse = bxClient.getResponse();

            for (Map.Entry obj : bxResponse.getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                String Id = String.valueOf(obj.getKey());
                HashMap<String, Object> fieldValueMap = (HashMap<String, Object>) obj.getValue();

                String entity = "<h3>" + Id + "</h3>";

                for (Map.Entry item : ((HashMap<String, Object>) fieldValueMap).entrySet()) {
                    entity += item.getKey() + ": " + String.join(",", (List<String>) (item.getValue()));
                }
                logs.add(entity);
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
