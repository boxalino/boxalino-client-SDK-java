/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import static Helper.Common.EMPTY_STRING;
import com.boxalino.p13n.api.thrift.FacetRequest;
import com.boxalino.p13n.api.thrift.FacetResponse;
import com.boxalino.p13n.api.thrift.FacetSortOrder;
import com.boxalino.p13n.api.thrift.FacetValue;
import com.boxalino.p13n.api.thrift.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxFacets {

    protected String parameterPrefix = "";
    protected String priceFieldName = "discountedPrice";
    private final HashMap<String, Filter> filters = new HashMap<>();
    public Map<String, FacetValue> selectedValues = new HashMap<>();
    public Map<String, Object> facets = new HashMap<>();
    public List<FacetResponse> facetResponse = new ArrayList<>();

    public HashMap<String, Filter> getFilters() {
        return this.filters;
    }

    private ArrayList<FacetValue> facetSelectedValue(String fieldName, String option) {
        ArrayList<FacetValue> selectedFacets = new ArrayList<>();
        if (this.facets != null) {
            facets.entrySet().stream().filter((value) -> (value.getKey() == fieldName)).map((value) -> {
                FacetValue selectedFacet = new FacetValue();
                if (option == "ranged") {
                    String[] rangedValue = value.getKey().split("-");
                    if (rangedValue[0] != "*") {
                        selectedFacet.rangeFromInclusive = rangedValue[0];
                    }
                    if (rangedValue[1] != "*") {
                        selectedFacet.rangeToExclusive = rangedValue[1];
                    }
                } else {
                    selectedFacet.stringValue = value.getKey();
                }
                return selectedFacet;
            }).forEachOrdered((selectedFacet) -> {
                selectedFacets.add(selectedFacet);
            });
            return selectedFacets;
        }
        return null;
    }

    public ArrayList<FacetRequest> getThriftFacets() {

        ArrayList<FacetRequest> thriftFacets = new ArrayList<>();
        facets.entrySet().stream().map((item) -> {
            String type = ((HashMap) item.getValue()).get("type").toString();
            int order = (Integer) (((HashMap) item.getValue()).get("order"));
            FacetRequest facetRequest = new FacetRequest();
            facetRequest.fieldName = item.getKey();
            facetRequest.numerical = (type == "ranged" ? true : (type == "numerical"));
            facetRequest.range = type == "ranged";
            facetRequest.boundsOnly = (Boolean) (((HashMap) item.getValue()).get("boundsOnly"));
            facetRequest.selectedValues = this.facetSelectedValue(item.getValue().toString(), type);
            facetRequest.sortOrder = (FacetSortOrder) ((order == 1) ? FacetSortOrder.COLLATION : FacetSortOrder.POPULATION);
            return facetRequest;
        }).forEachOrdered((facetRequest) -> {
            thriftFacets.add(facetRequest);
        });
        return thriftFacets;
    }

    public void addFacet(String fieldName, String selectedValue, String type, String label, int order, boolean boundsOnly) {

        //default start
        if (type.isEmpty()) {
            type = "string";
        }
        if (label.isEmpty()) {
            label = null;
        }
        if (order == 0) {
            order = 2;
        }
        boundsOnly = false;
        //default end 
        selectedValues = new HashMap<>();
        if (selectedValue != null) {
            selectedValues.put(selectedValue, new FacetValue());
        }
        Map<String, Object> fvalue = new HashMap<>();
        this.facets.put(fieldName, new HashMap());
        ((HashMap) this.facets.get(fieldName)).put("label", label);
        ((HashMap) this.facets.get(fieldName)).put("type", type);
        ((HashMap) this.facets.get(fieldName)).put("order", order);
        ((HashMap) this.facets.get(fieldName)).put("selectedValues", selectedValues);
        ((HashMap) this.facets.get(fieldName)).put("boundsOnly", boundsOnly);

    }

    public void setFacetResponse(List<FacetResponse> facetResponse) {
        this.facetResponse = facetResponse;
    }

    public void addRangedFacet(String fieldName, String selectedValue, String label, int order, boolean boundsOnly) {
        //default start
        if (order == 0) {
            order = 2;
        }
        //default end

        this.addFacet(fieldName, selectedValue, "ranged", label, order, boundsOnly);
    }

    public void addPriceRangeFacet(String selectedValue, int order, String label, String fieldName) {
        //default start
        if (order == 0) {
            order = 2;
        }
        if (label.isEmpty()) {
            label = "Price";
        }
        if (fieldName.isEmpty()) {
            fieldName = "discountedPrice";
        }
        //default end
        this.priceFieldName = fieldName;
        this.addRangedFacet(fieldName, selectedValue, label, order, true);
    }

    protected FacetResponse getFacetResponse(String fieldName) throws BoxalinoException {
        if (this.facetResponse != null) {
            for (FacetResponse facetResponsee : this.facetResponse) {
                if (facetResponsee.fieldName.equals(fieldName)) {
                    return facetResponsee;
                }
            }
        }
        throw new BoxalinoException("trying to get facet response on unexisting fieldname " + fieldName);
    }

    protected String getFacetType(String fieldName) {
        String type = "string";
        if (this.facets.get(fieldName) != null) {
            type = ((HashMap) this.facets.get(fieldName)).get("type").toString();
        }
        return type;
    }

    protected Map<String, Map<String, FacetValue>> buildTree(FacetResponse response, HashMap<String, String> parents, int parentLevel) {
        if (parents == null) {
            parents = new HashMap<>();
        }
        if (parents.isEmpty()) {
            for (FacetValue node : response.values) {
                if ((node.hierarchy).size() == 1) {
                    for (String nodeh : node.hierarchy) {
                        parents.put(String.valueOf(node.hierarchy.indexOf(nodeh)), nodeh);
                    }
                }
            }
        }
        Map<String, Map<String, FacetValue>> children = new HashMap<>();
        for (FacetValue node : response.values) {
            if ((node.hierarchy).size() == parentLevel + 2) {
                boolean allTrue = true;
                for (Map.Entry<String, String> item : parents.entrySet()) {
                    if ((!node.hierarchy.get(Integer.valueOf((String) item.getKey())).isEmpty()) || (node.hierarchy.get(Integer.valueOf((String) item.getKey())) != item.getValue())) {
                        allTrue = false;
                    }
                }
                if (allTrue) {
                    HashMap<String, String> childHierarchy = new HashMap<>();
                    node.hierarchy.forEach((nodeh) -> {
                        childHierarchy.put(String.valueOf(node.hierarchy.indexOf(nodeh)), nodeh);
                    });
                    children = (this.buildTree(response, childHierarchy, parentLevel + 1));
                }
            }
        }
        for (FacetValue node : response.values) {
            if (node.hierarchy.size() == parentLevel + 1) {
                boolean allTrue = true;
                Map<String, String> childHierarchy = new HashMap<>();
                for (HashMap.Entry<String, String> item : childHierarchy.entrySet()) {
                    if (parents.get(item.getKey()) != item.getValue()) {
                        allTrue = false;
                    }
                }
                if (allTrue) {
                    Map<String, Map<String, FacetValue>> buildresult = new HashMap<>();
                    Map<String, FacetValue> buildresultChild = new HashMap<>();
                    buildresultChild.put("children", (FacetValue) children.get("children"));
                    buildresult.put("node", buildresultChild);
                    return buildresult;
                }
            }
        }
        return null;
    }

    public Map<String, Map<String, FacetValue>> getSelectedTreeNode(Map<String, Map<String, FacetValue>> tree) {
        if ((this.facets.get("category_id")) != null) {
            return tree;
        }
        if (tree.get("node") == null) {
            return null;
        }
        List<String> parts = new ArrayList<>();
        parts.addAll(Arrays.asList(((FacetValue) ((HashMap) tree.get("node")).get("node")).stringValue.split("/")));

        if (parts.get(0) == ((HashMap) this.facets.get("category_id")).get("selectedValues").toString()) {
            return tree;
        }
        for (Map.Entry node : tree.get("children").entrySet()) {

            HashMap<String, Map<String, FacetValue>> selectedTreeNodetemp = new HashMap<>();
            Map<String, FacetValue> selectedTreeNodeFatemp = new HashMap<>();
            selectedTreeNodeFatemp.put(String.valueOf(node.getKey()), (FacetValue) node.getValue());
            selectedTreeNodetemp.put("node", selectedTreeNodeFatemp);
            Map<String, Map<String, FacetValue>> result = this.getSelectedTreeNode(selectedTreeNodetemp);

            if (result != null) {
                return result;
            }
        }
        return null;
    }

    protected Map<String, Map<String, FacetValue>> getFirstNodeWithSeveralChildren(Map<String, Map<String, FacetValue>> tree) {
        if (tree == null) {
            return null;
        }

        if (tree.get("children").isEmpty()) {
            return null;
        }
        if (tree.get("children").size() > 1) {
            return tree;
        }
        Map<String, FacetValue> treech = new HashMap<>();
        treech.put("node", ((FacetValue) ((HashMap) tree.get("children")).entrySet().toArray()[0]));
        tree = new HashMap<String, Map<String, FacetValue>>() {
            {
                put("children", treech);
            }
        };
        return this.getFirstNodeWithSeveralChildren(tree);
    }

    protected Map<String, FacetValue> getFacetKeysValues(String fieldName) throws BoxalinoException {
        if (fieldName == "") {
            return new HashMap<>();
        }
        Map<String, FacetValue> facetValues = new HashMap<>();

        FacetResponse facetResponsee = this.getFacetResponse(fieldName);

        String type = this.getFacetType(fieldName);
        switch (type) {
            case "hierarchical":
                Map<String, Map<String, FacetValue>> tree = this.buildTree(facetResponsee, null, 0);
                tree = this.getSelectedTreeNode(tree);
                Map<String, Map<String, FacetValue>> node = this.getFirstNodeWithSeveralChildren(tree);
                if (node != null) {
                    node.get("children").entrySet().forEach((fvalue) -> {
                        facetValues.put(fvalue.getKey() == "node" ? fvalue.getValue().stringValue : EMPTY_STRING, (FacetValue) node.get("node").values().toArray()[0]);
                    });
                }
                break;
            case "ranged":
                facetResponsee.values.forEach((facetValue) -> {
                    facetValues.put(facetValue.rangeFromInclusive + "-" + facetValue.rangeToExclusive, facetValue);
                });
                break;
            default:
                facetResponsee.values.forEach((facetValue) -> {
                    facetValues.put(facetValue.stringValue, facetValue);
                });
                break;
        }
        return facetValues;
    }

    public Map<String, FacetValue> getFacetValues(String fieldName) throws BoxalinoException {
        return this.getFacetKeysValues(fieldName);
    }

    protected List<String> getFacetValueArray(String fieldName, FacetValue facetValue) throws BoxalinoException {
        Map<String, FacetValue> keyValues = this.getFacetKeysValues(fieldName);
        if (keyValues.entrySet().toArray() == null) {
            throw new BoxalinoException("Requesting an invalid facet values for fieldname: " + fieldName + ", requested value: " + facetValue + ", available values . " + String.join(",", (keyValues).keySet()));
        }
        String type = this.getFacetType(fieldName);
        FacetValue fv = null;
        for (Map.Entry fvalue : keyValues.entrySet()) {
            if (fvalue.getValue().equals(facetValue)) {
                fv = (FacetValue) fvalue.getValue();
                break;
            }

        }
        switch (type) {
            case "hierarchical":
                List<String> parts = Arrays.asList(fv.stringValue.split("/"));
                String hitCount = String.valueOf(fv.hitCount);
                String selected = String.valueOf(fv.selected);
                return new ArrayList<String>() {
                    {
                        add((parts.toArray()[parts.size() - 1]).toString());
                        add(String.valueOf(parts.toArray()[0]));
                        add(hitCount);
                        add(selected);
                    }
                };
            case "ranged":
                double from = Math.round(Double.parseDouble(fv.rangeFromInclusive));
                double to = Math.round(Double.parseDouble(fv.rangeToExclusive));
                String valueLabel = from + " - " + to;
                String paramValue = fv.stringValue;
                paramValue = from + "-" + to;
                String par = paramValue;
                String HitCount = String.valueOf(fv.hitCount);
                String Selected = String.valueOf(fv.selected);
                return new ArrayList<String>() {
                    {
                        add(valueLabel);
                        add(par);
                        add(HitCount);
                        add(Selected);
                    }
                };

            default:
                fv = keyValues.get(facetValue);
                String StringValue = String.valueOf(fv.stringValue);
                String dHitCount = String.valueOf(fv.hitCount);
                String dSelected = String.valueOf(fv.selected);
                return new ArrayList<String>() {
                    {
                        add(StringValue);
                        add(StringValue);
                        add(dHitCount);
                        add(dSelected);
                    }
                };
        }
    }

    public String isFacetValueSelected(String fieldName, FacetValue facetValue) throws BoxalinoException {
        List<String> list = this.getFacetValueArray(fieldName, facetValue);
        String label = String.valueOf(list.toArray()[0]);
        String parameterValue = String.valueOf(list.toArray()[1]);
        String hitCount = String.valueOf(list.toArray()[2]);
        String selected = String.valueOf(list.toArray()[3]);
        return selected;
    }

    public Map<String, FacetValue> getSelectedValues(String fieldName) throws BoxalinoException {
        selectedValues = new HashMap<>();
        try {
            for (Map.Entry<String, FacetValue> key : this.getFacetValues(fieldName).entrySet()) {
                if (!this.isFacetValueSelected(fieldName, key.getValue()).isEmpty()) {
                    selectedValues.put(key.getKey(), new FacetValue());
                }
            }
        } catch (BoxalinoException ex) {
            throw ex;
        }
        return selectedValues;
    }

    public String getCategoryFieldName() {
        return "categories";
    }

    public void addCategoryFacet(String selectedValue, int order) {
        //default start
        if (order == 0) {
            order = 2;
        }
        //default end

        if ((selectedValue) != null) {
            this.addFacet("category_id", selectedValue, "hierarchical", "1", 2, false);
        }
        this.addFacet(this.getCategoryFieldName(), null, "hierarchical", String.valueOf(order), 2, false);
    }

    public void setParameterPrefix(String parameterPrefix) {
        this.parameterPrefix = parameterPrefix;
    }

    protected int isCategories(String fieldName) {
        return fieldName.indexOf("categories");
    }

    public String getFacetParameterName(String fieldName) {
        String parameterName = fieldName;
        if (this.isCategories(fieldName) > 0) {
            parameterName = "category_id";
        }
        return this.parameterPrefix + parameterName;
    }

    public List<String> getFieldNames() {
        return this.facets.size() > 0 ? Arrays.asList((String[]) (this.facets).keySet().toArray()) : new ArrayList<String>();
    }

    protected Map<String, Object> getFacetByFieldName(String fieldName) {
        for (Map.Entry<String, Object> item : this.facets.entrySet()) {
            if (fieldName == item.getKey()) {
                return (Map<String, Object>) item.getValue();
            }
        }
        return null;
    }

    public boolean isSelected(String fieldName, boolean ignoreCategories) throws BoxalinoException {
        if (fieldName == "") {
            return false;
        }
        if (this.isCategories(fieldName) > 0) {
            if (ignoreCategories) {
                return false;
            }
        }
        if (this.getSelectedValues(fieldName).size() > 0) {
            return true;
        }
        Map<String, Object> facet = this.getFacetByFieldName(fieldName);
        if (facet != null) {
            if (facet.get("type").toString() == "hierarchical") {
                FacetResponse facetResponsee = this.getFacetResponse(fieldName);
                Map<String, Map<String, FacetValue>> tree = this.buildTree(facetResponsee, null, 0);
                tree = this.getSelectedTreeNode(tree);
                return ((FacetValue) ((HashMap) tree.get("node")).get("node")).hierarchy.size() > 1;

            }

            return ((HashMap) this.facets.get(fieldName)).get("selectedValues") != null && Arrays.asList(((HashMap) this.facets.get(fieldName)).get("selectedValues")).size() > 0;

        }
        return false;
    }

    public Map<String, Map<String, FacetValue>> getTreeParent(Map<String, Map<String, FacetValue>> tree, Map<String, Map<String, FacetValue>> treeEnd) {
        for (Map.Entry child : tree.get("children").entrySet()) {

            if ((child.getKey().toString() == "node" ? ((FacetValue) child.getValue()) : EMPTY_STRING) == (((FacetValue) ((HashMap) treeEnd.get("node")).get("node")).stringValue)) {
                return tree;
            }

            Map<String, FacetValue> cFMap = new HashMap<>();
            cFMap.put("children", (FacetValue) child.getValue());
            Map<String, Map<String, FacetValue>> cParent = new HashMap<>();
            cParent.put("children", cFMap);
            Map<String, Map<String, FacetValue>> parent = this.getTreeParent(cParent, treeEnd);
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    public Map<String, Map<String, FacetValue>> getParentCategories() throws BoxalinoException {
        String fieldName = "categories";
        FacetResponse facetResponse = this.getFacetResponse(fieldName);
        Map<String, Map<String, FacetValue>> tree = this.buildTree(facetResponse, null, 0);
        Map<String, Map<String, FacetValue>> treeEnd = this.getSelectedTreeNode(tree);
        if (treeEnd == null) {
            return new HashMap<>();
        }
        if ((((FacetValue) ((HashMap) treeEnd.get("node")).get("node")).stringValue) == (((FacetValue) ((HashMap) tree.get("node")).get("node")).stringValue)) {
            return new HashMap<>();
        }
        Map<String, Map<String, FacetValue>> parent = new HashMap<>();
        parent = treeEnd;
        Map<String, String> parents = null;
        while (parent.size() > 0) {
            List<String> parts = Arrays.asList(((FacetValue) ((HashMap) parent.get("node")).get("node")).stringValue.split("/"));
            parents = new HashMap<String, String>() {
                {
                    put("0", String.valueOf(parts.get(0)));
                    put("1", parts.get((parts).size() - 1));
                }
            };
            parent = this.getTreeParent(tree, parent);
        }
        parents = parents;
        Map<String, Map<String, FacetValue>> tfinal = new HashMap<>();
        for (Map.Entry<String, String> v : parents.entrySet()) {
            ((FacetValue) ((HashMap) tfinal.get(v.getKey())).get(v.getKey())).stringValue = v.getValue();
        }
        return tfinal;
    }

    public long getParentCategoriesHitCount(int id) throws BoxalinoException {
        String fieldName = "categories";
        FacetResponse facetResponsee = this.getFacetResponse(fieldName);
        Map<String, Map<String, FacetValue>> tree = this.buildTree(facetResponsee, null, 0);
        Map<String, Map<String, FacetValue>> treeEnd = this.getSelectedTreeNode(tree);
        if (treeEnd == null) {
            return ((FacetValue) ((HashMap) tree.get("node")).get("node")).hitCount;
        }
        if ((((FacetValue) ((HashMap) treeEnd.get("node")).get("node")).stringValue) == (((FacetValue) ((HashMap) tree.get("node")).get("node")).stringValue)) {
            return ((FacetValue) ((HashMap) tree.get("node")).get("node")).hitCount;
        }
        Map<String, Map<String, FacetValue>> parent = treeEnd;
        while (parent.size() > 0) {
            if (Integer.parseInt(((FacetValue) ((HashMap) tree.get("node")).get("node")).hierarchyId) == id) {
                return Integer.parseInt(((FacetValue) ((HashMap) tree.get("node")).get("node")).hierarchyId);
            }
            parent = this.getTreeParent(tree, parent);
        }
        return 0;
    }

    public String getFacetValueLabel(String fieldName, FacetValue facetValue) throws BoxalinoException {
        List<String> item = this.getFacetValueArray(fieldName, facetValue);
        String label = String.valueOf(item.get(0));
        String parameterValue = String.valueOf(item.get(1));
        String hitCount = String.valueOf(item.get(2));
        String selected = String.valueOf(item.get(3));

        return label;
    }

    public String getSelectedValueLabel(String fieldName, int index) throws BoxalinoException {
        if (fieldName == "") {
            return "";
        }
        Map<String, FacetValue> svs = this.getSelectedValues(fieldName);
        if (String.valueOf(svs.keySet().toArray()[index]) != null) {
            return this.getFacetValueLabel(fieldName, ((FacetValue) svs.get(String.valueOf(svs.keySet().toArray()[index]))));
        }
        Map<String, Object> facet = this.getFacetByFieldName(fieldName);
        if (facet != null) {
            if (facet.get("type").toString() == "hierarchical") {
                FacetResponse facetResponsee = this.getFacetResponse(fieldName);
                Map<String, Map<String, FacetValue>> tree = this.buildTree(facetResponsee, null, 0);
                tree = this.getSelectedTreeNode(tree);
                String pl = ((FacetValue) ((HashMap) tree.get("node")).get("node")).stringValue;
                List<String> parts = new ArrayList<String>() {
                    {
                        add(pl);
                    }
                };
                return parts.toArray()[(parts).size() - 1].toString();
            }
            if (facet.get("type").toString() == "ranged") {
                if (((HashMap) this.facets.get(fieldName)).get("selectedValues") != null) {
                    return ((HashMap) this.facets.get(fieldName)).get("selectedValues").toString();
                }
            }
            if (facet.get("selectedValues") != null) {
                return facet.get("selectedValues").toString();
            }
            return "";
        }
        return "";
    }

    public String getPriceFieldName() {
        return this.priceFieldName;
    }

    public Map<String, FacetValue> getCategories() throws BoxalinoException {
        return this.getFacetValues(this.getCategoryFieldName());
    }

    public String getCategoryValueLabel(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueLabel(this.getCategoryFieldName(), facetValue);
    }

    public Map<String, FacetValue> getCategoriesKeyLabels() throws BoxalinoException {
        Map<String, FacetValue> categoryValueArray = new HashMap<>();
        for (Map.Entry v : this.getCategories().entrySet()) {
            String label = this.getCategoryValueLabel((FacetValue) v.getValue());
            categoryValueArray.put(label, (FacetValue) v.getValue());
        }
        return categoryValueArray;
    }

    public Map<String, FacetValue> getPriceRanges() throws BoxalinoException {
        return this.getFacetValues(this.getPriceFieldName());
    }

    public String getFacetLabel(String fieldName) {
        if (this.facets.get(fieldName) != null) {
            return ((HashMap) this.facets.get(fieldName)).get("label").toString();
        }
        return fieldName;
    }

    public String getPriceValueLabel(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueLabel(this.getPriceFieldName(), facetValue);
    }

    public String getFacetValueCount(String fieldName, FacetValue facetValue) throws BoxalinoException {
        List<String> item = this.getFacetValueArray(fieldName, facetValue);
        String label = String.valueOf(item.toArray()[0]);
        String parameterValue = String.valueOf(item.toArray()[1]);
        String hitCount = String.valueOf(item.toArray()[2]);
        String selected = String.valueOf(item.toArray()[3]);
        return hitCount;
    }

    public String getCategoryValueCount(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueCount(this.getCategoryFieldName(), facetValue);
    }

    public String getPriceValueCount(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueCount(this.getPriceFieldName(), facetValue);
    }

    public String getFacetValueParameterValue(String fieldName, FacetValue facetValue) throws BoxalinoException {
        List<String> item = this.getFacetValueArray(fieldName, facetValue);
        String label = String.valueOf(item.toArray()[0]);
        String parameterValue = String.valueOf(item.toArray()[1]);
        String hitCount = String.valueOf(item.toArray()[2]);
        String selected = String.valueOf(item.toArray()[3]);
        return parameterValue;
    }

    public String getCategoryValueId(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueParameterValue(this.getCategoryFieldName(), facetValue);
    }

    public String getPriceValueParameterValue(FacetValue facetValue) throws BoxalinoException {
        return this.getFacetValueParameterValue(this.getPriceFieldName(), facetValue);
    }

    public String isPriceValueSelected(FacetValue facetValue) throws BoxalinoException {
        return this.isFacetValueSelected(this.getPriceFieldName(), facetValue);
    }

    public String getParentId(String fieldName, String id) {
        List<String> hierarchy = new ArrayList<>();
        for (FacetResponse response : this.facetResponse) {
            if (response.fieldName == fieldName) {
                for (FacetValue item : response.values) {
                    if (item.hierarchyId == id) {
                        hierarchy = item.hierarchy;
                        if (hierarchy.size() < 4) {
                            return "1";
                        }
                    }
                }
                for (FacetValue item : response.values) {
                    if (item.hierarchy.size() == hierarchy.size() - 1) {
                        if (item.hierarchy.toArray()[hierarchy.size() - 2] == hierarchy.toArray()[hierarchy.size() - 2]) {
                            return item.hierarchyId;
                        }
                    }
                }
            }
        }
        return null;
    }

}
