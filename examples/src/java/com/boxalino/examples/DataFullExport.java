/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import static Helper.Common.EMPTY_STRING;
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
public class DataFullExport {

    public String _account;
    public String _password;
    public boolean _print;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void dataFullExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        new HttpContext().request = request;
        new HttpContext().response = response;
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
 /* TODO output your page here. You may use following sample code. */
 /*In this example, we take a very simple CSV file with product data, generate the specifications, load them, publish them and push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            String account = this._account; // your account name
            String password = this._password; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = this._print;

            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            String file = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/products.csv");
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item
            String colorFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/color.csv");
            String colorIdColumn = "color_id"; //column header row name of the csv with the unique category id
            Map<String, Object> colorLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };  //column header row names of the csv with the category label in each language

            String productToColorsFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/product_color.csv");

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
            String categoryFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/categories.csv");
            String categoryIdColumn = "category_id"; //column header row name of the csv with the unique category id
            String parentCategoryIdColumn = "parent_id"; //column header row name of the csv with the parent category id

            Map<String, Object> categoryLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };
            String productToCategoriesFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/SampleData/product_categories.csv");
            //add a csv file with products ids to categories ids
            String productToCategoriesSourceKey = bxData.addCSVItemFile(productToCategoriesFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with categories
            bxData.addCategoryFile(categoryFile, categoryIdColumn, parentCategoryIdColumn, categoryLabelColumns, "", "", "", "", "", "", "", true);
            bxData.setCategoryField(productToCategoriesSourceKey, categoryIdColumn, "", true);

            //Customer export
            String customerFile = request.getServletContext().getRealPath("/WEB-INF/Resources/SampleData/customers.csv");
            String customerIdColumn = "customer_id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main customer file
            String customerSourceKey = bxData.addMainCSVCustomerFile(customerFile, customerIdColumn, "", "", "", "", "", "", "", true);
            bxData.addSourceStringField(customerSourceKey, "country", "country", "", true);
            bxData.addSourceStringField(customerSourceKey, "zip", "zip", "", true);

            //Transaction export
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
