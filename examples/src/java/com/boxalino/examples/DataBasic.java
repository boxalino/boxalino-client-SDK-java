/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.examples;

import Exception.BoxalinoException;
import boxalino.client.SDK.BxClient;
import boxalino.client.SDK.BxData;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author HASHIR
 */
public class DataBasic {

    String account; // your account name
    String password; // your account password
    String domain; // your web-site domain (e.g.: www.abc.com)
    String[] languages; //declare the list of available languages
    boolean isDev; //are the data to be pushed dev or prod data?
    boolean isDelta; //are the data to be pushed full data (reset index) or delta (add/modify index)?
    List<String> logs; //optional, just used here in example to collect logs
    boolean print = true;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @throws IOException if an I/O error occurs
     */
    public void dataBasic() throws IOException {

        
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
            this.print = true;
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);
            String file = new File(GetDirectory() + "/WEB-INF/Resources/SampleData/products.csv").getPath();
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main product file
            String sourceKey = bxData.addMainCSVItemFile(file, itemIdColumn, "", "", "", "", "", "", "", true);

            //this part is only necessary to do when you push your data in full, as no specifications changes should not be published without a full data sync following next
            //even when you publish your data in full, you don't need to repush your data specifications if you know they didn't change, however, it is totally fine (and suggested) to push them everytime if you are not sure if something changed or not
            if (!isDelta) {

                //declare the fields
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

    public String GetDirectory() {
        String Directory = "";
        try {
            String path = this.getClass().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            Directory = pathArr[0];
        } catch (Exception ex) {

        }
        return Directory;
    }

}
