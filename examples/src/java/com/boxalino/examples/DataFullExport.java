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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class DataFullExport {

    public String account; // your account name
    public String password; // your account password
    String domain; // your web-site domain (e.g.: www.abc.com)
    String[] languages; //declare the list of available languages
    boolean isDev; //are the data to be pushed dev or prod data?
    boolean isDelta; //are the data to be pushed full data (reset index) or delta (add/modify index)?
    List<String> logs; //optional, just used here in example to collect logs
    public boolean print;
    private String ip="";
    private String referer="";
    private String currentUrl="";    
    private String userAgent = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @throws IOException if an I/O error occurs
     */
    public void dataFullExport() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
 /* TODO output your page here. You may use following sample code. */
 /*In this example, we take a very simple CSV file with product data, generate the specifications, load them, publish them and push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            this.account = "java_unittest"; // your account name
            this.password = "java_unittest"; // your account password
            this.domain = ""; // your web-site domain (e.g.: www.abc.com)
            this.languages = new String[]{"en"}; //declare the list of available languages
            this.isDev = false; //are the data to be pushed dev or prod data?
            this.isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            this.logs = new ArrayList<>(); //optional, just used here in example to collect logs
            this.print = true;
            //Create HttpContext instance
            HttpContext httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null), languages, isDev, isDelta);

            //To get the real path of the files
            String path = this.getClass().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            String Directory = pathArr[0];

            String file = new File(Directory + "/WEB-INF/Resources/SampleData/products.csv").getPath(); //a csv file with header row

            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item
            String colorFile = new File(Directory + "/WEB-INF/Resources/SampleData/color.csv").getPath();
            String colorIdColumn = "color_id"; //column header row name of the csv with the unique category id
            Map<String, Object> colorLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };  //column header row names of the csv with the category label in each language

            String productToColorsFile = new File(Directory + "/WEB-INF/Resources/SampleData/product_color.csv").getPath();

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
            String categoryFile = new File(Directory + "/WEB-INF/Resources/SampleData/categories.csv").getPath();
            String categoryIdColumn = "category_id"; //column header row name of the csv with the unique category id
            String parentCategoryIdColumn = "parent_id"; //column header row name of the csv with the parent category id

            Map<String, Object> categoryLabelColumns = new HashMap<String, Object>() {
                {
                    put("en", "value_en");
                }
            };
            String productToCategoriesFile = new File(Directory + "/WEB-INF/Resources/SampleData/product_categories.csv").getPath();
            //add a csv file with products ids to categories ids
            String productToCategoriesSourceKey = bxData.addCSVItemFile(productToCategoriesFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with categories
            bxData.addCategoryFile(categoryFile, categoryIdColumn, parentCategoryIdColumn, categoryLabelColumns, "", "", "", "", "", "", "", true);
            bxData.setCategoryField(productToCategoriesSourceKey, categoryIdColumn, "", true);

            //Customer export
            String customerFile = new File(Directory + "/WEB-INF/Resources/SampleData/customers.csv").getPath();
            String customerIdColumn = "customer_id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main customer file
            String customerSourceKey = bxData.addMainCSVCustomerFile(customerFile, customerIdColumn, "", "", "", "", "", "", "", true);
            bxData.addSourceStringField(customerSourceKey, "country", "country", "", true);
            bxData.addSourceStringField(customerSourceKey, "zip", "zip", "", true);

            //Transaction export
            String transactionFile = new File(Directory + "/WEB-INF/Resources/SampleData/transactions.csv").getPath();
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
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
