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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class DataFullExport extends HttpServlet {

    public String account;
    public String password;
    public boolean print;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpContext.request = request;
        HttpContext.response = response;
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
 /* TODO output your page here. You may use following sample code. */
 /*In this example, we take a very simple CSV file with product data, generate the specifications, load them, publish them and push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            account = "java_unittest"; // your account name
            password = "java_unittest"; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            print = true;

            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            String file = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\products.csv").getPath();
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item
            String colorFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\color.csv").getPath();
            String colorIdColumn = "color_id"; //column header row name of the csv with the unique category id
            Map<String, Object> colorLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };  //column header row names of the csv with the category label in each language

            String productToColorsFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\product_color.csv").getPath();

            //add a csv file as main product file
            String sourceKey = bxData.addMainCSVItemFile(file, itemIdColumn, "", "", "", "", "", "", "", true);
            bxData.addSourceStringField(sourceKey, "related_product_ids", "related_product_ids", null, true);
            bxData.addFieldParameter(sourceKey, "related_product_ids", "splitValues", ",");

            bxData.addSourceTitleField(sourceKey, new HashMap<String, String>() {
                {
                    put("en", "name_en");
                }
            }, null, true);

            bxData.addSourceDescriptionField(sourceKey, new HashMap<String, String>() {
                {
                    put("en", "description_en");
                }
            }, null, true);

            bxData.addSourceListPriceField(sourceKey, "list_price", null, true);
            bxData.addSourceDiscountedPriceField(sourceKey, "discounted_price", null, true);

            bxData.addSourceLocalizedTextField(sourceKey, "short_description", new HashMap<String, String>() {
                {
                    put("en", "short_description_en");
                }
            }, null, true);

            bxData.addSourceStringField(sourceKey, "sku", "sku", null, true);

            //add a csv file with products ids to Colors ids
            String productToColorsSourceKey = bxData.addCSVItemFile(productToColorsFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with Colors
            String colorSourceKey = bxData.addResourceFile(colorFile, colorIdColumn, colorLabelColumns, "", "", "", "", "", "", "", true);
            bxData.addSourceLocalizedTextField(productToColorsSourceKey, "color", colorIdColumn, colorSourceKey, true);

            //Category export
            String categoryFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\categories.csv").getPath();
            String categoryIdColumn = "category_id"; //column header row name of the csv with the unique category id
            String parentCategoryIdColumn = "parent_id"; //column header row name of the csv with the parent category id

            Map<String, Object> categoryLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };
            String productToCategoriesFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\product_categories.csv").getPath();
            //add a csv file with products ids to categories ids
            String productToCategoriesSourceKey = bxData.addCSVItemFile(productToCategoriesFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with categories
            bxData.addCategoryFile(categoryFile, categoryIdColumn, parentCategoryIdColumn, categoryLabelColumns, "", "", "", "", "", "", "", true);
            bxData.setCategoryField(productToCategoriesSourceKey, categoryIdColumn, "", true);

            //Customer export
            String customerFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\customers.csv").getPath();
            String customerIdColumn = "customer_id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main customer file
            String customerSourceKey = bxData.addMainCSVCustomerFile(customerFile, customerIdColumn, "", "", "", "", "", "", "", true);
            bxData.addSourceStringField(customerSourceKey, "country", "country", "", true);
            bxData.addSourceStringField(customerSourceKey, "zip", "zip", "", true);

            //Transaction export
            String transactionFile = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\transactions.csv").getPath();
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

            //add a csv file as main customer file
            bxData.setCSVTransactionFile(transactionFile, orderIdColumn, transactionProductIdColumn, transactionCustomerIdColumn, orderDateIdColumn, totalOrderValueColumn, productListPriceColumn, productDiscountedPriceColumn, transactionProductIdField, transactionCustomerIdField, "", "", "", "", "", "", "", "", "", "", true);

            //prepare autocomplete index
            bxData.prepareCorpusIndex("");

            List<String> fields = new ArrayList<String>() {
                {
                    add("products_color");
                }
            };
            bxData.prepareAutocompleteIndex(fields, "");

            logs.add("publish the data specifications");
            bxData.pushDataSpecifications(false);

            logs.add("publish the api owner changes"); //if the specifications have changed since the last time they were pushed
            bxData.publishChanges();

            logs.add("push the data for data sync");
            bxData.pushData(null);

            if (print) {

                out.print("<html><body>");
                out.print(String.join("<br>", logs));
                out.print("</body></html>");
            }

        } catch (BoxalinoException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        } catch (ParserConfigurationException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        } catch (TransformerException ex) {
            PrintWriter out = response.getWriter();
            out.print("<html><body>");
            out.print(String.join("<br>", ex.getMessage()));
            out.print("</body></html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
