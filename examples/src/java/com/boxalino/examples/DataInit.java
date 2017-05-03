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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HASHIR
 */
public class DataInit {

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
     * @throws IOException if an I/O error occurs
     */
    public void dataInit() throws IOException {
        
        
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
            HttpContext httpContext = new HttpContext(domain,userAgent,ip,referer,currentUrl);
            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null,httpContext), languages, isDev, isDelta);

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
            List<String> fields = new ArrayList<String>() {
                {
                    add("products_color");
                }
            };
            logs.add("force the preparation of an autocompletion index based on all the terms of the last data you sent ==> you need to have published your data before and you will need to publish them again that the corpus is sent to the index<br>");
            bxData.prepareAutocompleteIndex(fields, "");

            if (print) {
                System.out.println(String.join("\n", logs));

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }
}
