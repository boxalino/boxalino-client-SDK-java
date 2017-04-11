/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import com.boxalino.p13n.api.thrift.Hit;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public BxParametrizedRequest(String language, String choiceId, int max, int min, List<String> bxReturnFields, String getItemFieldsCB) throws BoxalinoException {
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
        String requestParametersPrefixx = this.requestParametersPrefix;
        String requestWeightedParametersPrefixx = this.requestWeightedParametersPrefix;
        String requestFiltersPrefixx = this.requestFiltersPrefix;
        String requestFacetsPrefixx= this.requestFacetsPrefix;
        String requestSortFieldPrefixx = this.requestSortFieldPrefix;
        return new ArrayList<String>() {
            {
                add(requestParametersPrefixx);
                add(requestWeightedParametersPrefixx);
                add(requestFiltersPrefixx);
                add(requestFacetsPrefixx);
                add(requestSortFieldPrefixx);
            }
        };
    }

    public boolean matchesPrefix(String _string, String prefix, boolean checkOtherPrefixes) {
        //default start
        checkOtherPrefixes = true;
        //default end
        if (checkOtherPrefixes) {
            if (!this.getPrefixes().stream().filter((p) -> !(p == prefix)).noneMatch((p) -> (prefix.length() < p.length() && _string.indexOf(p) == 0))) {
                return false;
            }
        }
        return prefix == null || _string.indexOf(prefix) == 0;
    }

    public Map<String, Object> getPrefixedParameters(String prefix, boolean checkOtherPrefixes) {
        //default start
        checkOtherPrefixes = true;
        //default end

        Map<String, Object> _params = new HashMap<>();
        for (Map.Entry<String, String> k : this.requestMap.entrySet()) {
            if (this.matchesPrefix(k.getKey(), prefix, checkOtherPrefixes)) {
                _params.put(k.getKey().substring(prefix.length()), k.getValue());
            }
        }
        return _params;
    }

    @Override
    public Map<String, ArrayList<String>> getRequestContextParameters() {
        HashMap<String, ArrayList<String>> _params = new HashMap<>();
        getPrefixedParameters(requestWeightedParametersPrefix, true).entrySet().forEach((item) -> {
            _params.put(item.getKey(), (ArrayList<String>) item.getValue());
        });
        getPrefixedParameters(requestParametersPrefix, false).entrySet().stream().filter((item) -> !(Common.strpos(item.getKey(), requestWeightedParametersPrefix, 0) != -1)).filter((item) -> !(Common.strpos(item.getKey(), requestFiltersPrefix, 0) != -1)).filter((item) -> !(Common.strpos(item.getKey(), requestFacetsPrefix, 0) != -1)).filter((item) -> !(Common.strpos(item.getKey(), requestSortFieldPrefix, 0) != -1)).filter((item) -> !(item.getKey().equals(requestReturnFieldsName))).forEachOrdered((item) -> {
            _params.put(item.getKey(), (ArrayList<String>) item.getValue());
        });

        return _params;
    }

    public Map<String, Object> getWeightedParameters() {
        Map<String, Object> _params = new HashMap<>();

        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestWeightedParametersPrefix, false).entrySet()) {
            String[] pieces = obj.getKey().split(" ");
            String fieldValue = "";
            if (pieces.length > 0) {
                fieldValue = pieces[pieces.length - 1];
                pieces[pieces.length - 1] = null;
            }
            String fieldName = String.join(" ", pieces);
            if (_params.get(fieldName) != null) {
                _params.put(fieldName, new HashMap<>());
            }
            _params.put(fieldName, new HashMap<>());

            ((HashMap) _params.get(fieldName)).put(fieldValue, obj.getValue());

        }
        return _params;
    }

    @Override
    public Map<String, BxFilter> getFilters() {
        Map<String, BxFilter> filters = super.getFilters();
        for (Map.Entry<String, Object> obj : this.getPrefixedParameters(this.requestFiltersPrefix, false).entrySet()) {
            boolean negative = false;
            String value = "";
            if ((obj.getValue().toString()).indexOf('!') == 0) {
                negative = true;
                value = (obj.getValue().toString()).substring(1);
            }

            ArrayList<String> bxValues = new ArrayList<>();
            bxValues.add(value);
            BxFilter bxFilter = new BxFilter(obj.getKey(), bxValues, negative);
            filters.put("", bxFilter);
        }
        return filters;
    }

    @Override
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

    @Override
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

    @Override
    public ArrayList<String> getReturnFields() {
        if (super.returnFields == null) {
            super.returnFields = new ArrayList<>();
        }
        super.returnFields.addAll(this.bxReturnFields);
        return (ArrayList<String>)super.returnFields.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getAllReturnFields() {
        List<String> returnFieldss = this.getReturnFields();
        if (this.requestMap.get(this.requestReturnFieldsName) != null) {
            super.getReturnFields().addAll(this.bxReturnFields);
            returnFieldss.addAll(Arrays.asList(this.requestMap.get(this.requestReturnFieldsName).split(",")));
        }
        return returnFieldss;
    }

    public Map<String, Object> retrieveFromCallBack(List<Hit> items, String[] fields) {
        if (this.callBackCache == null) {
            this.callBackCache = new HashMap<>();
            List<String> ids;
            ids = new ArrayList<>();
            items.forEach((item) -> {
                ids.addAll(item.values.get("id"));
            });
        }
        return this.callBackCache;
    }

    @Override
    public List<Map<String, Object>> retrieveHitFieldValues(Hit item, String field, List<Hit> items, String[] fields) {
        Map<String, Object> itemFields = this.retrieveFromCallBack(items, fields);
        List<Map<String, Object>> temp = new ArrayList<>();
        temp.add(itemFields);
        if (((HashMap) temp.get(0)).get("id") != null) {
            return (List<Map<String, Object>>) ((HashMap) temp.get(0)).get("id");
        }
        return super.retrieveHitFieldValues(item, field, items, fields);
    }

}
