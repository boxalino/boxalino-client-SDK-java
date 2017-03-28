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
public class DataTransactions {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void dataTransactions(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<String> logs = new ArrayList<String>();
            //optional, just used here in example to collect logs
            boolean print = true;
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            String productFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/products.csv");
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item

            String customerFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/customers.csv");
            String customerIdColumn = "customer_id"; //the column header row name of the csv with the unique id of each item

            String transactionFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/transactions.csv");
            String orderIdColumn = "order_id"; //the column header row name of the csv with the order (or transaction) id
            String transactionProductIdColumn = "product_id"; //the column header row name of the csv with the product id
            String transactionCustomerIdColumn = "customer_id"; //the column header row name of the csv with the customer id
            String orderDateIdColumn = "order_date"; //the column header row name of the csv with the order date
            String totalOrderValueColumn = "total_order_value"; //the column header row name of the csv with the total order value
            String productListPriceColumn = "price"; //the column header row name of the csv with the product list price
            String productDiscountedPriceColumn = "discounted_price"; //the column header row name of the csv with the product price after discounts (real price paid)

            //optional fields, provided here with default values (so, no effect if not provided), matches the field to connect to the transaction product id and customer id columns (if the ids are not the same as the itemIdColumn of your products and customers files, then you can define another field)
            String transactionProductIdField = "bx_item_id"; //default value (can be left null) to define a specific field to map with the product id column
            String transactionCustomerIdField = "bx_customer_id"; //default value (can be left null) to define a specific field to map with the product id column

            //add a csv file as main product file
            bxData.addMainCSVItemFile(productFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file as main customer file
            bxData.addMainCSVCustomerFile(customerFile, customerIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file as main customer file
            bxData.setCSVTransactionFile(transactionFile, orderIdColumn, transactionProductIdColumn, transactionCustomerIdColumn, orderDateIdColumn, totalOrderValueColumn, productListPriceColumn, productDiscountedPriceColumn, transactionProductIdField, transactionCustomerIdField, "", "", "", "", "", "", "", "", "", "", true);

            //this part is only necessary to do when you push your data in full, as no specifications changes should not be published without a full data sync following next
            //even when you publish your data in full, you don't need to repush your data specifications if you know they didn't change, however, it is totally fine (and suggested) to push them everytime if you are not sure if something changed or not
            if (!isDelta) {

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
