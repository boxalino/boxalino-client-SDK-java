/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Helper.HttpContext;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class DataInit {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void dataInit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */

            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
             */
            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            String account = "java_unittest"; // your account name
            String password = "java_unittest"; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList();

            //optional, just used here in example to collect logs
            boolean print = true;
            //Create the Boxalino Data SDK instance
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            /**
             * Publish choices
             */
            //your choie configuration can be generated in 3 possible ways: dev (using dev data), prod (using prod data as on your live web-site), prod-test (using prod data but not affecting your live web-site)
            boolean isTest = false;

            String temp_isDev = isDev == false ? "" : "True";
            logs.add("force the publish of your choices configuration: it does it either for dev or prod (above " + temp_isDev + " parameter) and, if isDev is false, you can do it in prod or prod-test<br>");
            bxData.publishChoices(isTest, "");
            /**
             * Prepare corpus index
             */
            logs.add("force the preparation of a corpus index based on all the terms of the last data you sent ==> you need to have published your data before and you will need to publish them again that the corpus is sent to the index<br>");
            bxData.prepareCorpusIndex("");

            /**
             * Prepare autocomplete index
             */
            //NOT YET READY NOTICE: prepareAutocompleteIndex doesn't add the fields yet even if you pass them to the function like in this example here (TODO), for now, you need to go in the data intelligence admin and set the fields manually. You can contact support@boxalino.com to do that.
            //the autocomplete index is automatically filled with common searches done over time, but of course, before going live, you will not have any. While it is possible to load pre-existing search logs (contact support@boxalino.com to learn how, you can also define some fields which will be considered for the autocompletion anyway (e.g.: brand, product line, etc.).
            List<String> fields = new ArrayList() {
                {
                    add("products_color");
                }
            };
            logs.add("force the preparation of an autocompletion index based on all the terms of the last data you sent ==> you need to have published your data before and you will need to publish them again that the corpus is sent to the index<br>");
            bxData.prepareAutocompleteIndex(fields, "");

            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (FileNotFoundException ex) {

            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        }
    }

}
