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
public class DataResource {

    String account; // your account name
    String password; // your account password
    String domain; // your web-site domain (e.g.: www.abc.com)
    String[] languages; //declare the list of available languages
    boolean isDev; //are the data to be pushed dev or prod data?
    boolean isDelta; //are the data to be pushed full data (reset index) or delta (add/modify index)?
    List<String> logs; //optional, just used here in example to collect logs
    boolean print;
    private String ip="";
    private String referer="";
    private String currentUrl="";    
    private String userAgent = "";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     *
     * @throws IOException if an I/O error occurs
     */
    public void dataResource() throws IOException {

        try {
            /* TODO output your page here. You may use following sample code. */
            /**
             * In this example, we take a very simple CSV file with product
             * data, generate the specifications, load them, publish them and
             * push the data to Boxalino Data Intelligence
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
            this.print = true;//optional, just used here in example to collect logs
            //Create HttpContext instance
            HttpContext httpContext =  new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext, null, null), languages, isDev, isDelta);

            //To get the real path of the files
            String path = this.getClass().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            String Directory = pathArr[0];

            String mainProductFile = new File(Directory + "/WEB-INF/Resources/SampleData/products.csv").getPath(); //a csv file with header row            

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
            String mainSourceKey = bxData.addMainCSVItemFile(mainProductFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with products ids to Colors ids
            String productToColorsSourceKey = bxData.addCSVItemFile(productToColorsFile, itemIdColumn, "", "", "", "", "", "", "", true);

            //add a csv file with Colors
            String colorSourceKey = bxData.addResourceFile(colorFile, colorIdColumn, colorLabelColumns, "", "", "", "", "", "", "", true);

            //this part is only necessary to do when you push your data in full, as no specifications changes should not be published without a full data sync following next
            //even when you publish your data in full, you don't need to repush your data specifications if you know they didn't change, however, it is totally fine (and suggested) to push them everytime if you are not sure if something changed or not
            if (!isDelta) {

                //declare the color field as a localized textual field with a resource source key
                bxData.addSourceLocalizedTextField(productToColorsSourceKey, "color", colorIdColumn, colorSourceKey, true);

                logs.add("publish the data specifications");
                bxData.pushDataSpecifications(false);

                logs.add("publish the api owner changes"); //if the specifications have changed since the last time they were pushed
                bxData.publishChanges();

            }
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
