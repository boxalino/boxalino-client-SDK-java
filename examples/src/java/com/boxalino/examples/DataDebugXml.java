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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author HASHIR
 */
public class DataDebugXml extends HttpServlet {

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
 /*In this example, we take a very simple CSV file with product data, generate the specifications, load them, publish them and push the data to Boxalino Data Intelligence
             */

            //path to the lib folder with the Boxalino Client SDK and C# Thrift Client files
            //required parameters you should set for this example to work
            String account = "java_unittest"; // your account name
            String password = "java_unittest"; // your account password
            String domain = ""; // your web-site domain (e.g.: www.abc.com)
            String[] languages = new String[]{"en"}; //declare the list of available languages
            boolean isDev = false; //are the data to be pushed dev or prod data?
            boolean isDelta = false; //are the data to be pushed full data (reset index) or delta (add/modify index)?
            List<String> logs = new ArrayList<String>(); //optional, just used here in example to collect logs
            boolean print = true;

            //Create the Boxalino Data SDK instance
            BxData bxData = new BxData(new BxClient(account, password, domain, isDev, null, 0, null, null, null, null), languages, isDev, isDelta);

            String file = new File("E:\\Github\\BoxalinoJava\\boxalino-client-SDK-java\\SampleData\\products.csv").getPath();
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
                out.print("<html><body>");
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
                out.print("<textarea style=\"margin: 0px; width: 818px; height: 279px;\">" + writer.toString() + "</textarea>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
