/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import static Helper.Common.EMPTY_STRING;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author HASHIR
 */
public class BxData {

    String URL_VERIFY_CREDENTIALS = "/frontend/dbmind/en/dbmind/api/credentials/verify";
    String URL_XML = "/frontend/dbmind/en/dbmind/api/data/source/update";
    String URL_PUBLISH_CONFIGURATION_CHANGES = "/frontend/dbmind/en/dbmind/api/configuration/publish/owner";
    String URL_ZIP = "/frontend/dbmind/en/dbmind/api/data/push";
    String URL_EXECUTE_TASK = "/frontend/dbmind/en/dbmind/files/task/execute";

    private final BxClient bxClient;
    private String[] languages;
    private final boolean isDev;
    private final boolean isDelta;
    Map<String, String> sourceIdContainers = new HashMap<>();
    private final Map<String, Object> sources = new HashMap<>();
    Map<String, Object> ftpSources = new HashMap<>();
    private final String host = "http://di1.bx-cloud.com";
    private final String owner = "bx_client_data_api";

    public BxData(BxClient bxClient, String[] languages, boolean isDev, boolean isDelta) {
        this.bxClient = bxClient;
        this.languages = languages;
        this.isDev = isDev;
        this.isDelta = isDelta;
    }

    public String[] decodeSourceKey(String sourceKey) {
        return sourceKey.split("-");
    }

    public String[] getLanguages() {
        return this.languages;
    }

    public Object getSourceCSVRow(String container, String sourceId, int row, int maxRow) throws FileNotFoundException, IOException {
        //default start
        maxRow = 2;
        //default end
        Map<String, Object> source = (Map<String, Object>) ((HashMap) this.sources.get(container)).get(sourceId);
        source.put("rows", new HashMap<>());
        if (source.get("rows") != null) {
            int count = 1;
            BufferedReader reader = new BufferedReader(new FileReader(source.get("filePath").toString()));
            List<String> listA = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                listA.add(values[0]);
                if (count++ >= maxRow) {
                    break;
                }
            }

            source.put("rows", listA.get(0).split(","));

        }
        if (source.get("rows") != null) {
            return source.get("rows");
        }
        return null;
    }

    public void validateColumnExistance(String container, String sourceId, Object col) throws BoxalinoException, IOException {
        Object rows = getSourceCSVRow(container, sourceId, 0, 0);

        int in_array = 0;
        if (rows != null) {
            String[] row = ((String[]) rows);
            for (String item : row) {
                if (item.equals(col.toString())) {
                    in_array++;
                    break;
                }
            }
            if (in_array == 0) {
                throw new BoxalinoException("the source " + sourceId + " in the container " + container + " declares an column " + col + " which is not present in the header row of the provided CSV file: " + String.join(",", row));
            }
        }

    }

    public void addSourceField(String sourceKey, String fieldName, String type, boolean localized, Object colMap, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {

        String container = this.decodeSourceKey(sourceKey)[0].trim();
        String sourceId = this.decodeSourceKey(sourceKey)[1].trim();

        if (((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields") == null) {
            ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).put("fields", new HashMap<String, Object>());

        }

        ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).put(fieldName, new HashMap<String, Object>());
        ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get(fieldName)).put("type", type);
        ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get(fieldName)).put("localized", localized);
        ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get(fieldName)).put("map", colMap);
        ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get(fieldName)).put("referenceSourceKey", referenceSourceKey);

        Map<String, Object> source = (Map<String, Object>) ((HashMap) this.sources.get(container)).get(sourceId);

        if (source.get("format").toString() == "CSV") {
            if (localized && referenceSourceKey == null) {
                try {
                    Map<String, String> temp_colMap = (Map<String, String>) colMap;

                    for (String lang : this.getLanguages()) {
                        if (temp_colMap.get(lang) == null) {
                            throw new BoxalinoException(fieldName + " : no language column provided for language " + lang + " in provided column map): " + temp_colMap.keySet());
                        }

                        if (temp_colMap.get(lang) instanceof String) {

                        } else {
                            throw new BoxalinoException(fieldName + " : invalid column field name for a non-localized field (expect a string): " + temp_colMap.keySet());
                        }

                        if (validate) {
                            this.validateColumnExistance(container, sourceId, temp_colMap.get(lang));
                        }
                    }
                } catch (BoxalinoException ex) {
                    throw new BoxalinoException(fieldName + " : invalid column field name for a localized field (expect an array with a column name for each language array(lang=>colName)): " + String.valueOf(colMap));
                }

            } else {
                if (String.valueOf(colMap) == null) {
                    throw new BoxalinoException(fieldName + ": invalid column field name for a non-localized field (expect a string): " + String.valueOf(colMap));
                }
                if (validate) {
                    this.validateColumnExistance(container, sourceId, colMap);
                }
            }
        }
    }

    public void addSourceStringField(String sourceKey, String fieldName, Object col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, fieldName, "string", false, col, referenceSourceKey, validate);
    }

    public void addSourceNumberField(String sourceKey, String fieldName, String col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, fieldName, "number", false, col, referenceSourceKey, validate);
    }

    public String getFileNameFromPath(String filePath, boolean withoutExtension) {
        String[] parts;
        parts = filePath.split("\\\\");
        String file = parts[parts.length - 1];
        if (withoutExtension) {
            String[] ext = file.split("\\.");
            return ext[0].trim();
        }
        return file;
    }

    public void validateSource(String container, String sourceId) throws IOException, BoxalinoException {
        try {

            Map<String, Object> source = (Map<String, Object>) ((HashMap) this.sources.get(container)).get(sourceId);

            if (source.get("format").toString() == "CSV") {
                if (source.containsKey("itemIdColumn")) {
                    this.validateColumnExistance(container, sourceId, source.get("itemIdColumn"));
                }
            }
        } catch (BoxalinoException ex) {
            throw ex;
        }
    }

    public String encodesourceKey(String container, String sourceId) {
        return container + " -" + sourceId;
    }

    public String addSourceFile(String filePath, String sourceId, String container, String type, String format, Object parameters, boolean validate) throws BoxalinoException, IOException {
        //default start
        if (format == EMPTY_STRING) {
            format = "CSV";
        }
        //default end

        if (this.getLanguages().length == 0) {
            throw new BoxalinoException("trying to add a source before having declared the languages with method setLanguages");
        }
        if (this.sources.isEmpty()) {
            this.sources.put(container, new HashMap<>());
        }
        Map<String, Object> temp_parameters = (Map<String, Object>) parameters;
        temp_parameters.put("filePath", filePath);
        temp_parameters.put("format", format);
        temp_parameters.put("type", type);

        ((HashMap) this.sources.get(container)).put(sourceId, temp_parameters);

        if (validate) {
            this.validateSource(container, sourceId);
        }
        this.sourceIdContainers.put(sourceId, container);
        return this.encodesourceKey(container, sourceId);
    }

    public String addCSVItemFile(String filePath, String itemIdColumn, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws BoxalinoException, IOException {
        //default start

        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }

        if (delimiter.isEmpty()) {

            delimiter = ",";
        }

        if (enclosure.isEmpty()) {
            enclosure = "&";
        }
        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (container.isEmpty()) {
            container = "products";
        }

        //default end 
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("itemIdColumn", itemIdColumn);
        parameters.put("encoding", encoding);
        parameters.put("delimiter", delimiter);
        parameters.put("enclosure", enclosure);
        parameters.put("escape", escape);
        parameters.put("lineSeparator", lineSeparator);

        if (sourceId.isEmpty()) {
            sourceId = this.getFileNameFromPath(filePath, true);
        }
        return this.addSourceFile(filePath, sourceId, container, "item_data_file", "CSV", parameters, validate);
    }

    public void addSourceIdField(String sourceKey, Object col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {

        this.addSourceField(sourceKey, "bx_id", "id", false, col, referenceSourceKey, validate);
    }

    public String addMainCSVItemFile(String filePath, String itemIdColumn, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws BoxalinoException, IOException {
        //default start
        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "\"";
        }
        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (sourceId.isEmpty()) {
            sourceId = "item_vals";
        }
        if (container.isEmpty()) {
            container = "products";
        }

        //default end
        String sourceKey = this.addCSVItemFile(filePath, itemIdColumn, encoding, delimiter, enclosure, escape, lineSeparator, sourceId, container, validate);
        this.addSourceIdField(sourceKey, itemIdColumn, null, validate);
        this.addSourceStringField(sourceKey, "bx_item_id", itemIdColumn, null, validate);
        return sourceKey;
    }

    public void addFieldParameter(String sourceKey, String fieldName, String parameterName, String parameterValue) throws BoxalinoException {
        String container = this.decodeSourceKey(sourceKey)[0].trim();
        String sourceId = this.decodeSourceKey(sourceKey)[1].trim();

        if (((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields").toString() == EMPTY_STRING) {
            throw new BoxalinoException("trying to add a field parameter on sourceId " + sourceId + ", container " + container + ", fieldName " + fieldName + " while this field doesn't exist");
        }

        if (((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get("fieldName")).get("fieldParameters").toString() == EMPTY_STRING) {
            ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get("fieldName")).put("fieldParameters", new HashMap<>());

        }
        ((HashMap) ((HashMap) ((HashMap) ((HashMap) ((HashMap) this.sources.get(container)).get(sourceId)).get("fields")).get("fieldName")).get("fieldParameters")).put(parameterName, parameterValue);

    }

    public void setFieldIsMultiValued(String sourceKey, String fieldName, boolean multiValued) throws BoxalinoException {
        this.addFieldParameter(sourceKey, fieldName, "multiValued", multiValued ? "true" : "false");
    }

    public void addSourceParameter(String sourceKey, String parameterName, String parameterValue) throws BoxalinoException {
        String container = this.decodeSourceKey(sourceKey)[0].trim();
        String sourceId = this.decodeSourceKey(sourceKey)[1].trim();
        if (((HashMap) sources.get(container)).get(sourceId) == null) {
            throw new BoxalinoException("trying to add a source parameter on sourceId " + sourceId + ", container " + container + " while this source doesn't exist");
        }
        ((HashMap) ((HashMap) ((HashMap) this.sources.get(container))).get(sourceId)).put(parameterName, parameterValue);
    }

    public void addSourceCustomerGuestProperty(String sourceKey, String parameterValue) throws BoxalinoException {
        this.addSourceParameter(sourceKey, "guest_property_id", parameterValue);
    }

    public void setFtpSource(String sourceKey, String host, int port, String user, String password, String remoteDir, int protocol, int type, int logontype, int timezoneoffset, String pasvMode, int maximumMultipeConnections, String encodingType, int bypassProxy, int syncBrowsing) {
        //default start
        if (host.isEmpty()) {
            host = "di1.bx-cloud.com";
        }
        if (port == 0) {
            port = 21;
        }
        if (remoteDir.isEmpty()) {
            remoteDir = "/sources/production";
        }
        if (logontype == 0) {
            logontype = 1;
        }
        if (pasvMode.isEmpty()) {
            pasvMode = "MODE_DEFAULT";
        }
        if (encodingType.isEmpty()) {
            encodingType = "Auto";
        }
        //default end

        if (user == null) {
            user = this.bxClient.getAccount(false);
        }

        if (password == null) {
            password = this.bxClient.getPassword();
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Host", host);
        parameters.put("Port", port);
        parameters.put("User", user);
        parameters.put("Pass", password);
        parameters.put("Protocol", protocol);
        parameters.put("Type", type);
        parameters.put("Logontype", logontype);
        parameters.put("TimezoneOffset", timezoneoffset);
        parameters.put("PasvMode", pasvMode);
        parameters.put("MaximumMultipleConnections", maximumMultipeConnections);
        parameters.put("EncodingType", encodingType);
        parameters.put("BypassProxy", bypassProxy);
        parameters.put("Name", user + " at " + host);
        parameters.put("RemoteDir", remoteDir);
        parameters.put("SyncBrowsing", syncBrowsing);
        String container = this.decodeSourceKey(sourceKey)[0].trim();
        String sourceId = this.decodeSourceKey(sourceKey)[1].trim();
        this.ftpSources.put(sourceId, parameters);
    }

    public String addMainCSVCustomerFile(String filePath, String itemIdColumn, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws BoxalinoException, IOException {
        //default start
        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "\\&";
        }
        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (sourceId.isEmpty()) {
            sourceId = "customers";
        }
        if (container.isEmpty()) {
            container = "customers";
        }
        //default end 

        String sourceKey = this.addCSVItemFile(filePath, itemIdColumn, encoding, delimiter, enclosure, escape, lineSeparator, sourceId, container, validate);
        this.addSourceIdField(sourceKey, itemIdColumn, null, validate);
        this.addSourceStringField(sourceKey, "bx_customer_id", itemIdColumn, null, validate);
        return sourceKey;
    }

    public void addSourceTitleField(String sourceKey, Object colMap, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, "bx_title", "title", true, colMap, referenceSourceKey, validate);
    }

    public void addSourceDescriptionField(String sourceKey, Object colMap, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, "bx_description", "body", true, colMap, referenceSourceKey, validate);
    }

    public void addSourceListPriceField(String sourceKey, String col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, "bx_listprice", "price", false, col, referenceSourceKey, validate);
    }

    public void addSourceDiscountedPriceField(String sourceKey, String col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, "bx_discountedprice", "discounted", false, col, referenceSourceKey, validate);
    }

    public void addSourceLocalizedTextField(String sourceKey, String fieldName, Object colMap, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        this.addSourceField(sourceKey, fieldName, "text", true, colMap, referenceSourceKey, validate);
    }

    public String addCategoryFile(String filePath, String categoryIdColumn, String parentIdColumn, Map<String, Object> categoryLabelColumns, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws BoxalinoException, IOException {
        //default  start
        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }

        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "&";
        }
        if (escape.isEmpty()) {
            escape = "\\\\";
        }

        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (sourceId.isEmpty()) {
            sourceId = "resource_categories";
        }

        if (container.isEmpty()) {
            container = "products";
        }

        //default end
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("referenceIdColumn", categoryIdColumn);
        parameters.put("parentIdColumn", parentIdColumn);
        parameters.put("labelColumns", categoryLabelColumns);
        parameters.put("encoding", encoding);
        parameters.put("delimiter", delimiter);
        parameters.put("enclosure", enclosure);
        parameters.put("escape", escape);
        parameters.put("lineSeparator", lineSeparator);

        return this.addSourceFile(filePath, sourceId, container, "hierarchical", "CSV", parameters, validate);
    }

    public String addResourceFile(String filePath, String categoryIdColumn, Object labelColumns, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws IOException, BoxalinoException {
        //default start

        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "\\&";
        }

        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (container.isEmpty()) {
            container = "products";
        }

        //default end
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("referenceIdColumn", categoryIdColumn);
        parameters.put("labelColumns", labelColumns);
        parameters.put("encoding", encoding);
        parameters.put("delimiter", delimiter);
        parameters.put("enclosure", enclosure);
        parameters.put("escape", escape);
        parameters.put("lineSeparator", lineSeparator);

        if (sourceId == null) {
            sourceId = "resource_" + this.getFileNameFromPath(filePath, true);
        }
        return this.addSourceFile(filePath, sourceId, container, "resource", "CSV", parameters, validate);
    }

    public String setCSVTransactionFile(String filePath, String orderIdColumn, String productIdColumn, String customerIdColumn, String orderDateIdColumn, String totalOrderValueColumn, String productListPriceColumn, String productDiscountedPriceColumn, String productIdField, String customerIdField, String productsContainer, String customersContainer, String format, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String container, String sourceId, boolean validate) throws BoxalinoException, IOException {
        //default start

        if (productIdField.isEmpty()) {
            productIdField = "bx_item_id";
        }
        if (customerIdField.isEmpty()) {
            customerIdField = "bx_customer_id";
        }
        if (productsContainer.isEmpty()) {
            productsContainer = "products";
        }
        if (customersContainer.isEmpty()) {
            customersContainer = "customers";
        }
        if (format.isEmpty()) {
            format = "CSV";
        }
        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "\\&";
        }

        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (container.isEmpty()) {
            container = "transactions";
        }
        if (sourceId.isEmpty()) {
            sourceId = "transactions";
        }
        //default end

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("encoding", encoding);
        parameters.put("delimiter", delimiter);
        parameters.put("enclosure", enclosure);
        parameters.put("escape", escape);
        parameters.put("lineSeparator", lineSeparator);
        parameters.put("file", this.getFileNameFromPath(filePath, false));
        parameters.put("orderIdColumn", orderIdColumn);
        parameters.put("productIdColumn", productIdColumn);
        parameters.put("product_property_id", productIdField);
        parameters.put("customerIdColumn", customerIdColumn);
        parameters.put("customer_property_id", customerIdField);
        parameters.put("productListPriceColumn", productListPriceColumn);
        parameters.put("productDiscountedPriceColumn", productDiscountedPriceColumn);
        parameters.put("totalOrderValueColumn", totalOrderValueColumn);
        parameters.put("orderReceptionDateColumn", orderDateIdColumn);

        return this.addSourceFile(filePath, sourceId, container, "transactions", format, parameters, validate);
    }

    public String addCSVCustomerFile(String filePath, String itemIdColumn, String encoding, String delimiter, String enclosure, String escape, String lineSeparator, String sourceId, String container, boolean validate) throws BoxalinoException, IOException {
        //default start
        if (encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        if (delimiter.isEmpty()) {
            delimiter = ",";
        }
        if (enclosure.isEmpty()) {
            enclosure = "\\&";
        }
        if (escape.isEmpty()) {
            escape = "\\\\";
        }
        if (lineSeparator.isEmpty()) {
            lineSeparator = "\\n";
        }
        if (container.isEmpty()) {
            container = "customers";
        }

        //default end
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("itemIdColumn", itemIdColumn);
        parameters.put("encoding", encoding);
        parameters.put("delimiter", delimiter);
        parameters.put("enclosure", enclosure);
        parameters.put("escape", escape);
        parameters.put("lineSeparator", lineSeparator);

        if (sourceId == null) {
            sourceId = this.getFileNameFromPath(filePath, true);
        }
        return this.addSourceFile(filePath, sourceId, container, "item_data_file", "CSV", parameters, validate);
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public void setCategoryField(String sourceKey, String col, String referenceSourceKey, boolean validate) throws BoxalinoException, IOException {
        //defaul start 
        if (referenceSourceKey.isEmpty()) {
            referenceSourceKey = "resource_categories";
        }
        //default end

        if (referenceSourceKey == "resource_categories") {
            String container = decodeSourceKey(sourceKey)[0].trim();
            String sourceId = decodeSourceKey(sourceKey)[1].trim();
            referenceSourceKey = this.encodesourceKey(container, referenceSourceKey);
        }
        this.addSourceField(sourceKey, "category", "hierarchical", false, col, referenceSourceKey, validate);

    }

    public Document getXML() throws ParserConfigurationException, BoxalinoException, TransformerException, TransformerException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlDocument = dBuilder.newDocument();

        Element rootXML = xmlDocument.createElement("root");
        xmlDocument.appendChild(rootXML);
        Element languagesXML = xmlDocument.createElement("languages");
        rootXML.appendChild(languagesXML);
        for (String lang : this.getLanguages()) {
            Element languageXML = xmlDocument.createElement("language");
            languageXML.setAttribute("id", lang);
            languagesXML.appendChild(languageXML);

        }
        Element containersXML = xmlDocument.createElement("containers");
        rootXML.appendChild(containersXML);
        for (Map.Entry<String, Object> containerSources : this.sources.entrySet()) {

            for (Map.Entry<String, Object> sourceValues : ((Map<String, Object>) containerSources.getValue()).entrySet()) {

                Element containerXML = xmlDocument.createElement("container");

                containerXML.setAttribute("id", String.valueOf(containerSources.getKey()));
                containerXML.setAttribute("type", String.valueOf(containerSources.getKey()));
                containersXML.appendChild(containerXML);

                Element sourcesXML = xmlDocument.createElement("sources");
                containerXML.appendChild(sourcesXML);

                Element propertiesXML = xmlDocument.createElement("properties");
                containerXML.appendChild(propertiesXML);

                String sourceId = sourceValues.getKey();

                Element sourceXML = xmlDocument.createElement("source");

                Map<String, Object> temp_sourceValues = (Map<String, Object>) ((HashMap) this.sources.get(containerSources.getKey())).get(sourceId);

                sourceXML.setAttribute("id", sourceId);
                sourceXML.setAttribute("type", temp_sourceValues.get("type").toString());
                sourcesXML.appendChild(sourceXML);

                temp_sourceValues.put("file", this.getFileNameFromPath(String.valueOf(temp_sourceValues.get("filePath")), false));

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("file", false);
                parameters.put("format", "CSV");
                parameters.put("encoding", "UTF-8");
                parameters.put("delimiter", ",");
                parameters.put("enclosure", "\"");
                parameters.put("escape", "\\\\");
                parameters.put("lineSeparator", "\\n");

                switch (String.valueOf(temp_sourceValues.get("type"))) {
                    case "item_data_file":
                        parameters.put("itemIdColumn", false);
                        break;

                    case "hierarchical":
                        parameters.put("referenceIdColumn", false);
                        parameters.put("parentIdColumn", false);
                        parameters.put("labelColumns", false);
                        break;

                    case "resource":
                        parameters.put("referenceIdColumn", false);
                        parameters.put("itemIdColumn", false);
                        parameters.put("labelColumns", false);
                        temp_sourceValues.put("itemIdColumn", temp_sourceValues.get("referenceIdColumn"));
                        break;

                    case "transactions":
                        parameters.clear();
                        for (Map.Entry<String, Object> param : temp_sourceValues.entrySet()) {
                            parameters.put(param.getKey(), param.getValue());
                        }

                        parameters.remove("filePath");
                        parameters.remove("type");
                        parameters.remove("product_property_id");
                        parameters.remove("customer_property_id");

                        break;
                }
                Element parameter = null;

                for (Map.Entry<String, Object> defaultValue : parameters.entrySet()) {
                    Object value = temp_sourceValues.get(defaultValue.getKey()) != null ? temp_sourceValues.get(defaultValue.getKey()) : defaultValue.getValue();
                    try {
                        if (value.toString().isEmpty()) {
                            throw new BoxalinoException("source parameter " + defaultValue.getKey() + " required but not defined in source id " + sourceId + " for container '$containerName'");
                        }
                    } catch (BoxalinoException ex) {
                        throw ex;
                    }

                    Element xml_parameter = xmlDocument.createElement(defaultValue.getKey());
                    if (parameter != null) {
                        parameter = (Element) parameter.appendChild(xml_parameter);

                    } else {
                        parameter = (Element) sourceXML.appendChild(xml_parameter);
                    }

                    if (value instanceof HashMap) {
                        for (String lang : this.getLanguages()) {
                            Map<String, Object> temp_value = (Map<String, Object>) value;
                            Element languageParamXML = xmlDocument.createElement("language");

                            languageParamXML.setAttribute("name", lang);
                            languageParamXML.setAttribute("value", temp_value.get(lang).toString());
                            parameter.appendChild(languageParamXML);
                        }

                    } else {

                        parameter.setAttribute("value", String.valueOf(value));
                    }

                    if (temp_sourceValues.get("type").toString() == "transactions") {
                        switch (String.valueOf(defaultValue.getKey())) {
                            case "productIdColumn":
                                parameter.setAttribute("product_property_id", String.valueOf(temp_sourceValues.get("product_property_id")));

                                break;

                            case "customerIdColumn":

                                parameter.setAttribute("customer_property_id", String.valueOf(temp_sourceValues.get("customer_property_id")));

                                if (temp_sourceValues.containsKey("guest_property_id")) {
                                    parameter.setAttribute("guest_property_id", String.valueOf(temp_sourceValues.get("guest_property_id")));
                                }
                                break;
                        }
                    }
                }

                if (!this.ftpSources.isEmpty() && String.valueOf(this.ftpSources.get(sourceId)) != null) {

                    Element locationXML = xmlDocument.createElement("location");
                    parameter.appendChild(locationXML);
                    parameter.setAttribute("type", "ftp");

                    Element ftpXML = xmlDocument.createElement("ftp");
                    parameter.appendChild(locationXML);
                    ftpXML.setAttribute("name", "ftp");

                    ((Map<String, Object>) ftpSources.get(sourceId)).entrySet().forEach((ftpPv) -> {
                        String ftpPn = ftpPv.getKey();
                    });
                }

                if (((HashMap) ((HashMap) this.sources.get(containerSources.getKey())).get(sourceId)).containsKey("fields")) {

                    for (Map.Entry<String, Object> fieldValues : ((Map<String, Object>) ((HashMap) ((HashMap) this.sources.get(containerSources.getKey())).get(sourceId)).get("fields")).entrySet()) {

                        String fieldId = fieldValues.getKey();
                        Map<String, Object> temp_fieldValues = (Map<String, Object>) fieldValues.getValue();

                        Element propertyXML = xmlDocument.createElement("property");

                        propertyXML.setAttribute("id", fieldId);
                        propertyXML.setAttribute("type", temp_fieldValues.get("type").toString());
                        propertiesXML.appendChild(propertyXML);

                        Element transformXML = xmlDocument.createElement("transform");
                        propertyXML.appendChild(transformXML);

                        Element logicXML = xmlDocument.createElement("logic");
                        transformXML.appendChild(logicXML);

                        String referenceSourceKey = (temp_fieldValues.get("referenceSourceKey") != null) ? String.valueOf(temp_fieldValues.get("referenceSourceKey")) : null;
                        String logicType = referenceSourceKey == null || referenceSourceKey == "" ? "direct" : "reference";
                        if (logicType == "direct") {
                            if (temp_fieldValues.containsKey("fieldParameters")) {
                                for (Map.Entry<String, Object> parameterValue : ((Map<String, Object>) temp_fieldValues.get("fieldParameters")).entrySet()) {
                                    String parameterName = parameterValue.getKey();
                                    switch (parameterName) {
                                        case "pc_fields":
                                        case "pc_tables":
                                            logicType = "advanced";
                                            break;
                                    }
                                }
                            }
                        }

                        logicXML.setAttribute("type", logicType);
                        logicXML.setAttribute("source", sourceId);
                        Element fieldXML = xmlDocument.createElement("field");
                        logicXML.appendChild(fieldXML);

                        if (temp_fieldValues.get("map") instanceof HashMap) {
                            for (String lang : this.getLanguages()) {

                                Map<String, String> temp_colMap = (Map<String, String>) temp_fieldValues.get("map");

                                fieldXML.setAttribute("column", temp_colMap.get(lang));
                                fieldXML.setAttribute("language", lang);
                            }
                        } else {

                            fieldXML.setAttribute("column", String.valueOf(temp_fieldValues.get("map")));
                        }

                        Element paramsXML = xmlDocument.createElement("params");
                        propertyXML.appendChild(paramsXML);

                        if (referenceSourceKey != null) {
                            Element referenceSourceXML = xmlDocument.createElement("referenceSource");

                            paramsXML.appendChild(referenceSourceXML);
                            String referenceContainer = decodeSourceKey(referenceSourceKey)[0].trim();
                            String referenceSourceId = decodeSourceKey(referenceSourceKey)[1].trim();
                            referenceSourceXML.setAttribute("value", referenceSourceId);
                        }
                        if (temp_fieldValues.containsKey("fieldParameters")) {
                            ((Map<String, Object>) temp_fieldValues.get("fieldParameters")).entrySet().stream().map((parameterValue) -> parameterValue.getKey()).forEachOrdered((parameterName) -> {
                                Element fieldParameterXML = xmlDocument.createElement("fieldParameter");
                                paramsXML.appendChild(fieldParameterXML);

                                fieldParameterXML.setAttribute("name", parameterName);
                                fieldParameterXML.setAttribute("value", String.valueOf(((HashMap) (temp_fieldValues.get("fieldParameters"))).get(parameterName)));
                            });
                        }
                    }
                }
            }
        }

        return xmlDocument;
    }

    protected Map<String, Object> callAPI(Map<String, Object> fields, String url, String temporaryFilePath) throws UnsupportedEncodingException, IOException, BoxalinoException {

        String responseFromServer;
        responseFromServer = EMPTY_STRING;

        String urlParameters = null;
        HttpURLConnection connection;
        connection = null;
        for (Map.Entry<String, Object> item : fields.entrySet()) {
            String value = String.valueOf(item.getValue());
            if (item.getKey() == "xml") {
                DOMSource domSource = new DOMSource((Document) item.getValue());
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
                value = writer.toString();
            }

            urlParameters += URLEncoder.encode(item.getKey(), "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8") + "&";

        }
        if (urlParameters.endsWith("&")) {
            urlParameters = urlParameters.substring(0, urlParameters.length() - 1);
        }
        //Create connection
        URL target_url = new URL(url);
        connection = (HttpURLConnection) target_url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        try ( //Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(urlParameters);
        }
        //Get Response
        InputStream is = connection.getInputStream();
        StringBuilder response;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
            response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
        } // or StringBuffer if Java version 5+
        responseFromServer = response.toString();
        if (connection != null) {
            connection.disconnect();
        }

        return this.checkResponseBody(responseFromServer, url);
    }

    public Map<String, Object> pushDataSpecifications(boolean ignoreDeltaException) throws BoxalinoException, ParserConfigurationException, IOException, TransformerException {

        if (!ignoreDeltaException && this.isDelta) {
            throw new BoxalinoException("You should not push specifications when you are pushing a delta file. Only do it when you are preparing full files. Set method parameter ignoreDeltaException to true to ignore this exception and publish anyway.");
        }

        Map<String, Object> fields = new HashMap<>();

        fields.put("username", this.bxClient.getUsername());
        fields.put("password", this.bxClient.getPassword());

        fields.put("account", this.bxClient.getAccount(false));
        fields.put("owner", this.owner);
        fields.put("xml", this.getXML());
        String url = this.host + URL_XML;
        return callAPI(fields, url, null);
    }

    public Map<String, Object> checkResponseBody(String responseBody, String url) throws BoxalinoException {
        if (responseBody == null) {
            throw new BoxalinoException("API response of call to " + url + " is empty string, this is an error!");

        }

        Gson gson = new Gson();
        Map<String, Object> value = gson.fromJson(responseBody, new TypeToken<HashMap<String, Object>>() {
        }.getType());

       

        if (value.containsKey("token")) {
            if (value.containsKey("changes")) {
                if (value.get("changes").toString().length() > 2) {
                    throw new BoxalinoException(responseBody);
                }
            }
        }
        return value;
    }

    public Map<String, Object> getFiles() {
        Map<String, Object> files = new HashMap<>();
        this.sources.entrySet().forEach((containerSources) -> {
            String container = containerSources.getKey();
            Map<String, Object> temp_containerSources = (Map<String, Object>) containerSources.getValue();
            temp_containerSources.entrySet().stream().map((sourceValues) -> sourceValues.getKey()).forEachOrdered((sourceId) -> {
                Map<String, Object> temp_source = (Map<String, Object>) ((HashMap) this.sources.get(container)).get(sourceId);
                if (!(temp_source.containsKey(sourceId))) {
                    if (temp_source.containsKey("file")) {
                        temp_source.put("file", this.getFileNameFromPath(String.valueOf(temp_source.get("filePath")), false));

                    }
                    files.put(String.valueOf(temp_source.get("file")), temp_source.get("filePath"));
                }
            });
        });
        return files;
    }

    public String createZip(String temporaryFilePath, String name) throws FileNotFoundException, IOException, ParserConfigurationException, BoxalinoException, TransformerException {
        //default start
        if (name.isEmpty()) {
            name = "bxdata.zip";
        }
        //default end

        if (temporaryFilePath == null) {
            temporaryFilePath = System.getProperty("java.io.tmpdir") + "bxclient";
        }

        if (temporaryFilePath != "" && !new File(temporaryFilePath).exists()) {
            new File(temporaryFilePath).mkdir();
        }

        String zipFilePath = temporaryFilePath + "\\" + name;

        if (new File(zipFilePath).exists()) {
            Common.unlink(zipFilePath);
        }

        Map<String, Object> files = this.getFiles();
        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath); ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            for (Map.Entry<String, Object> file : files.entrySet()) {
                // a ZipEntry represents a file entry in the zip archive
                // We name the ZipEntry after the original file's name
                ZipEntry zipEntry = new ZipEntry(file.getKey());
                zipOutputStream.putNextEntry(zipEntry);

                FileInputStream fileInputStream = new FileInputStream(file.getValue().toString());
                byte[] buf = new byte[1024];
                int bytesRead;

                // Read the input file by chucks of 1024 bytes
                // and write the read bytes to the zip stream
                while ((bytesRead = fileInputStream.read(buf)) > 0) {
                    zipOutputStream.write(buf, 0, bytesRead);
                }

            }

            ZipEntry zipEntry = new ZipEntry("properties.xml");
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(getXML().toString().getBytes());

            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();

        }
        return zipFilePath;
    }

    protected String getCurlFile(String filename, String type) {

        return filename;
    }

    public Map<String, Object> pushData(String temporaryFilePath) throws IOException, UnsupportedEncodingException, BoxalinoException, FileNotFoundException, ParserConfigurationException, TransformerException {

        String zipFile = this.createZip(temporaryFilePath, EMPTY_STRING);

        Map<String, Object> fields = new HashMap<>();
        fields.put("username", this.bxClient.getUsername());
        fields.put("password", this.bxClient.getPassword());
        fields.put("account", this.bxClient.getAccount(false));
        fields.put("owner", this.owner);
        fields.put("dev", this.isDev ? "true" : "false");
        fields.put("delta", this.isDelta ? "true" : "false");
        fields.put("data", this.getCurlFile(zipFile, "application/zip"));

        String url = this.host + URL_ZIP;
        return this.callAPI(fields, url, temporaryFilePath);
    }

    public String getTaskExecuteUrl(String taskName) {
        return this.host + URL_EXECUTE_TASK + "?iframeAccount=" + this.bxClient.getAccount(true) + "&task_process=" + taskName;
    }

    public String prepareAutocompleteIndex(Object fields, String taskName) throws FileNotFoundException {
        if (taskName.isEmpty()) {
            taskName = "autocomplete";
        }

        String url = this.getTaskExecuteUrl(taskName);
        return Common.file_get_contents(url);
    }

    public void prepareCorpusIndex(String taskName) throws FileNotFoundException {
        //default start
        if (taskName.isEmpty()) {
            taskName = "corpus";
        }
        //defualt end 
        String url = this.getTaskExecuteUrl(taskName);
        Common.file_get_contents(url);
    }

    public void publishChoices(boolean isTest, String taskName) throws FileNotFoundException {
        //default start
        if (taskName.isEmpty()) {
            taskName = "generate_optimization";
        }

        //default end 
        if (this.isDev) {
            taskName += "_dev";
        }
        if (isTest) {
            taskName += "_test";
        }
        String url = this.getTaskExecuteUrl(taskName);
        Common.file_get_contents(url);
    }

    public Map<String, Object> publishOwnerChanges(boolean publish) throws IOException, UnsupportedEncodingException, BoxalinoException {
        if (this.isDev) {
            publish = false;
        }
        Map<String, Object> fields = new HashMap<>();
        fields.put("username", this.bxClient.getUsername());
        fields.put("password", this.bxClient.getPassword());
        fields.put("account", this.bxClient.getAccount(false));
        fields.put("owner", this.owner);
        fields.put("publish", publish ? "true" : "false");

        String url = this.host + URL_PUBLISH_CONFIGURATION_CHANGES;
        return this.callAPI(fields, url, null);
    }

    public void checkChanges() throws IOException, UnsupportedEncodingException, BoxalinoException {
        this.publishOwnerChanges(false);
    }

    public Map<String, Object> verifyCredentials() throws IOException, UnsupportedEncodingException, BoxalinoException {
        Map<String, Object> fields = new HashMap<>();
        fields.put("username", this.bxClient.getUsername());
        fields.put("password", this.bxClient.getPassword());
        fields.put("account", this.bxClient.getAccount(false));
        fields.put("owner", this.owner);

        String url = this.host + URL_VERIFY_CREDENTIALS;
        return this.callAPI(fields, url, null);
    }

    public String getError(String responseBody) {
        return responseBody;
    }

    public void publishChanges() {
        try {
            this.publishOwnerChanges(true);
        } catch (IOException ex) {

        } catch (BoxalinoException ex) {

        }
    }

}
