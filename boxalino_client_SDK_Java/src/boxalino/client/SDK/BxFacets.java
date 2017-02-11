/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Helper.Common;
import com.boxalino.p13n.api.thrift.FacetRequest;
import com.boxalino.p13n.api.thrift.FacetResponse;
import com.boxalino.p13n.api.thrift.FacetSortOrder;
import com.boxalino.p13n.api.thrift.FacetValue;
import com.boxalino.p13n.api.thrift.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxFacets {

    private HashMap<String, Filter> filters = new HashMap<String, Filter>();
    public Map<String, FacetValue> selectedValues = new HashMap<String, FacetValue>();
    public Map<String, Object> facets = new HashMap<String, Object>();
    public List<FacetResponse> facetResponse = new ArrayList<FacetResponse>();

    public HashMap<String, Filter> getFilters() {
        return this.filters;
    }

    public ArrayList<FacetRequest> getThriftFacets() {

        ArrayList<FacetRequest> thriftFacets = new ArrayList<FacetRequest>();

        return thriftFacets;
    }

    public void addFacet(String fieldName, String selectedValue, String type, String label, int order, boolean boundsOnly) {

        //default start
        if (type == Common.EMPTY_STRING) {
            type = "string";
        }
        if (label == Common.EMPTY_STRING) {
            label = null;
        }
        if (order == 0) {
            order = 2;
        }
        boundsOnly = false;
        //default end 
        selectedValues = new HashMap<String, FacetValue>();
        if (selectedValue != null) {
            selectedValues.put(selectedValue, new FacetValue());
        }
        Map<String, Object> fvalue = new HashMap<String, Object>();
        ((HashMap) this.facets.get(fieldName)).put("label", label);
        ((HashMap) this.facets.get(fieldName)).put("type", type);
        ((HashMap) this.facets.get(fieldName)).put("order", order);
        ((HashMap) this.facets.get(fieldName)).put("selectedValues", selectedValues);
        ((HashMap) this.facets.get(fieldName)).put("boundsOnly", boundsOnly);

    }

    public void setFacetResponse(List<FacetResponse> facetResponse) {
        this.facetResponse = facetResponse;
    }

}
