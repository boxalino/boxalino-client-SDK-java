/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Helper.Common;
import com.boxalino.p13n.api.thrift.Hit;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxParametrizedRequest extends BxRequest {

    public List<String> bxReturnFields = new ArrayList<String>() {
        {
            add("id");
        }
    };
    public String getItemFieldsCB = null;
    public String requestParametersPrefix = "";
    public String requestWeightedParametersPrefix = "bxrpw_";
    public String requestFiltersPrefix = "bxfi_";
    public String requestFacetsPrefix = "bxfa_";
    public String requestSortFieldPrefix = "bxsf_";
    public String requestReturnFieldsName = "bxrf";
    private Map<String, Object> callBackCache = null;

    public BxParametrizedRequest(String language, String choiceId, int max, int min, List<String> bxReturnFields, String getItemFieldsCB) {
        super(language, choiceId, max == 0 ? 10 : max, min);
        if (bxReturnFields != null) {
            this.bxReturnFields = bxReturnFields;
        }
        this.getItemFieldsCB = getItemFieldsCB;
    }

    public void setRequestParametersPrefix(String requestParametersPrefix) {
        this.requestParametersPrefix = requestParametersPrefix;
    }

    public String getRequestParametersPrefix() {
        return this.requestParametersPrefix;
    }

    public void setRequestWeightedParametersPrefix(String requestWeightedParametersPrefix) {
        this.requestWeightedParametersPrefix = requestWeightedParametersPrefix;
    }

    public String getRequestWeightedParametersPrefix() {
        return this.requestWeightedParametersPrefix;
    }

    public void setRequestFiltersPrefix(String requestFiltersPrefix) {
        this.requestFiltersPrefix = requestFiltersPrefix;
    }

    public String getRequestFiltersPrefix() {
        return this.requestFiltersPrefix;
    }

    public void setRequestFacetsPrefix(String requestFacetsPrefix) {
        this.requestFacetsPrefix = requestFacetsPrefix;
    }

    public String getRequestFacetsPrefix() {
        return this.requestFacetsPrefix;
    }

    public void setRequestSortFieldPrefix(String requestSortFieldPrefix) {
        this.requestSortFieldPrefix = requestSortFieldPrefix;
    }

    public String getRequestSortFieldPrefix() {
        return this.requestSortFieldPrefix;
    }

    public void setRequestReturnFieldsName(String requestReturnFieldsName) {
        this.requestReturnFieldsName = requestReturnFieldsName;
    }

    public String getRequestReturnFieldsName() {
        return this.requestReturnFieldsName;
    }

    public List<String> getPrefixes() {
        String requestParametersPrefix = this.requestParametersPrefix;
        String requestWeightedParametersPrefix = this.requestWeightedParametersPrefix;
        String requestFiltersPrefix = this.requestFiltersPrefix;
        String requestFacetsPrefix = this.requestFacetsPrefix;
        String requestSortFieldPrefix = this.requestSortFieldPrefix;
        return new ArrayList<String>() {
            {
                add(requestParametersPrefix);
                add(requestWeightedParametersPrefix);
                add(requestFiltersPrefix);
                add(requestFacetsPrefix);
                add(requestSortFieldPrefix);
            }
        };
    }

    public boolean matchesPrefix(String _string, String prefix, boolean checkOtherPrefixes) {
        //default start
        checkOtherPrefixes = true;
        //default end
        if (checkOtherPrefixes) {
            for (String p : this.getPrefixes()) {
                if (p == prefix) {
                    continue;
                }
                if (prefix.length() < p.length() && _string.indexOf(p) == 0) {
                    return false;
                }
            }
        }
        return prefix == null || _string.indexOf(prefix) == 0;
    }

    public Map<String, Object> getPrefixedParameters(String prefix, boolean checkOtherPrefixes) {
        //default start
        checkOtherPrefixes = true;
        //default end

        Map<String, Object> _params = new HashMap<String, Object>();
        for (Map.Entry<String, String> k : this.requestMap.entrySet()) {
            if (this.matchesPrefix(k.getKey(), prefix, checkOtherPrefixes)) {
                _params.put(k.getKey().substring(prefix.length()), k.getValue());
            }
        }
        return _params;
    }

    public Map<String, ArrayList<String>> getRequestContextParameters() {
        HashMap<String, ArrayList<String>> _params = new HashMap<String, ArrayList<String>>();
        for (Map.Entry<String, Object> item : getPrefixedParameters(requestWeightedParametersPrefix, true).entrySet()) {
            _params.put(item.getKey(), (ArrayList<String>) item.getValue());
        }
        for (Map.Entry<String, Object> item : getPrefixedParameters(requestParametersPrefix, false).entrySet()) {
            if (Common.strpos(item.getKey(), requestWeightedParametersPrefix, 0) != -1) {
                continue;
            }
            if (Common.strpos(item.getKey(), requestFiltersPrefix, 0) != -1) {
                continue;
            }
            if (Common.strpos(item.getKey(), requestFacetsPrefix, 0) != -1) {
                continue;
            }
            if (Common.strpos(item.getKey(), requestSortFieldPrefix, 0) != -1) {
                continue;
            }
            if (item.getKey() == requestReturnFieldsName) {
                continue;
            }
            _params.put(item.getKey(), (ArrayList<String>) item.getValue());
        }

        return _params;
    }

    public Map<String, Object> getWeightedParameters() {
        Map<String, Object> _params = new HashMap<String, Object>();

        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestWeightedParametersPrefix, false).entrySet()) {
            String[] pieces = obj.getKey().toString().split(" ");
            String fieldValue = "";
            if (pieces.length > 0) {
                fieldValue = pieces[pieces.length - 1];
                pieces[pieces.length - 1] = null;
            }
            String fieldName = String.join(" ", pieces);
            if (_params.get(fieldName) != null) {
                _params.put(fieldName, new HashMap<String, Object>());
            }
            _params.put(fieldName, new HashMap<String, Object>());

            ((HashMap) _params.get(fieldName)).put(fieldValue, obj.getValue());

        }
        return _params;
    }

    public Map<String, BxFilter> getFilters() {
        Map<String, BxFilter> filters = super.getFilters();
        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestFiltersPrefix, false).entrySet()) {
            boolean negative = false;
            String value = "";
            if ((obj.getValue().toString()).indexOf('!') == 0) {
                negative = true;
                value = (obj.getValue().toString()).substring(1);
            }

            ArrayList<String> bxValues = new ArrayList<String>();
            bxValues.add(value);
            BxFilter bxFilter = new BxFilter(obj.getKey().toString(), bxValues, negative);
            filters.put("", bxFilter);
        }
        return filters;
    }

    public BxFacets getFacets() {
        BxFacets facets = super.getFacets();
        if (facets == null) {
            facets = new BxFacets();
        }
        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestFacetsPrefix, false).entrySet()) {
            facets.addFacet(obj.getKey(), obj.getValue().toString(), Common.EMPTY_STRING, Common.EMPTY_STRING, 0, false);
        }
        return facets;
    }

    public BxSortFields getSortFields() {
        BxSortFields sortFields = super.getSortFields();
        if (sortFields == null) {
            sortFields = new BxSortFields();
        }
        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestSortFieldPrefix, false).entrySet()) {
            sortFields.push(obj.getKey(),((Boolean)obj.getValue()));
        }
        return sortFields;
    }

    public ArrayList<String> getReturnFields() {
        if (super.returnFields == null) {
            super.returnFields = new ArrayList<String>();
        }
        super.returnFields.addAll(this.bxReturnFields);
        return super.returnFields;
    }

    public List<String> getAllReturnFields() {
        List<String> returnFields = this.getReturnFields();
        if (this.requestMap.get(this.requestReturnFieldsName) != null) {
            super.getReturnFields().addAll(this.bxReturnFields);
            returnFields.addAll(Arrays.asList(this.requestMap.get(this.requestReturnFieldsName).split(",")));
        }
        return returnFields;
    }

    public Map<String, Object> retrieveFromCallBack(List<Hit> items, String[] fields) {
        if (this.callBackCache == null) {
            this.callBackCache = new HashMap<String, Object>();
            List<String> ids = new ArrayList<String>();
            for (Hit item : items) {
                ids.addAll(item.values.get("id"));
            }
        }
        return this.callBackCache;
    }

    public List<Map<String, Object>> retrieveHitFieldValues(Hit item, String field, List<Hit> items, String[] fields) {
        Map<String, Object> itemFields = this.retrieveFromCallBack(items, fields);
        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
        temp.add(itemFields);
        if (((HashMap) temp.get(0)).get("id") != null) {
            return (List<Map<String, Object>>) ((HashMap) temp.get(0)).get("id");
        }
        return super.retrieveHitFieldValues(item, field, items, fields);
    }

}
