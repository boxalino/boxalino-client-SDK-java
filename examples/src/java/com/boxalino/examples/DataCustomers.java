/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import Helper.HttpContext;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxData;
import java.io.File;
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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author HASHIR
 */
public class DataCustomers {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void dataCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            String account = "java_unittest"; // your account name
            String password = "java_unittest"; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<>(); //optional, just used here in example to collect logs
            boolean print = true;

            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            String productFile =request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/products.csv"); //a csv file with header row
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item

            String customerFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/customers.csv"); //a csv file with header row
            String customerIdColumn = "customer_id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main product file
            bxData.addMainCSVItemFile(productFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file as main customer file
            String customerSourceKey = bxData.addMainCSVCustomerFile(customerFile, customerIdColumn, "", "", "", "", "", "", "", true);

            //this part is only necessary to do when you push your data in full, as no specifications changes should not be published without a full data sync following next
            //even when you publish your data in full, you don't need to repush your data specifications if you know they didn't change, however, it is totally fine (and suggested) to push them everytime if you are not sure if something changed or not
            if (!isDelta) {

                bxData.addSourceStringField(customerSourceKey, "country", "country", null, true);
                bxData.addSourceStringField(customerSourceKey, "zip", "zip", null, true);

                logs.add("publish the data specifications");
                bxData.pushDataSpecifications(false);

                logs.add("publish the api owner changes"); //if the specifications have changed since the last time they were pushed
                bxData.publishChanges();

            }

            logs.add("push the data for data sync");
            bxData.pushData(null);
            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {

            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        } catch (ParserConfigurationException ex) {

            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        } catch (TransformerException ex) {

            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        } catch (IOException ex) {

            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        }
    }

}
