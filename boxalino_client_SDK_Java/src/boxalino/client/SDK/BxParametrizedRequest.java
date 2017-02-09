/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Helper.Common;
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
    
    

}
