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
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author HASHIR
 */
public class DataDebugXml {

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
     * @throws IOException if an I/O error occurs
     */
    public void dataDebugXml() throws IOException {

        try {
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
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null, httpContext), languages, isDev, isDelta);

            //To get the real path of the files
            String path = this.getClass().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            String Directory = pathArr[0];

            String file = new File(Directory + "/WEB-INF/Resources/SampleData/products.csv").getPath();
            String itemIdColumn = "id"; //the column header row name of the csv with the unique id of each item

            //add a csv file as main product file
            String sourceKey = bxData.addMainCSVItemFile(file, itemIdColumn, "", "", "", "", "", "", "", true);

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

            if (print) {

                DOMSource domSource = new DOMSource(bxData.getXML());
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = null;
                try {
                    transformer = tf.newTransformer();
                    transformer.transform(domSource, result);
                } catch (TransformerConfigurationException ex) {

                } catch (TransformerException ex) {

                }
                System.out.println(writer.toString());

            }

        } catch (BoxalinoException ex) {

            System.out.println(ex.getMessage());

        }
    }

}
