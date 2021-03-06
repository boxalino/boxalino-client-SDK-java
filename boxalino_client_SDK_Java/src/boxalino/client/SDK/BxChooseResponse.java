/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import static Helper.Common.EMPTY_STRING;
import Helper.LevenshteinDistance;
import com.boxalino.p13n.api.thrift.ChoiceResponse;
import com.boxalino.p13n.api.thrift.Hit;
import com.boxalino.p13n.api.thrift.HitsGroup;
import com.boxalino.p13n.api.thrift.SearchResult;
import com.boxalino.p13n.api.thrift.Variant;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxChooseResponse {

    private final Object response;
    private final Object bxRequests;

    public BxChooseResponse(ChoiceResponse response, ArrayList<BxRequest> bxRequests) {
        this.response = response;
        if (bxRequests == null) {
            bxRequests = new ArrayList<>();
        }
        this.bxRequests = bxRequests;
    }

    public BxChooseResponse(SearchResult response, List<BxSearchRequest> bxRequests) {
        this.response = response;
        if (bxRequests == null) {
            bxRequests = new ArrayList<>();
        }
        this.bxRequests = bxRequests;
    }

    public Object getResponse() {
        return this.response;
    }

    protected Variant getChoiceIdResponseVariant(int id) throws BoxalinoException, Exception {
        Object response = getResponse();
        if (response instanceof ChoiceResponse) {
            ChoiceResponse cr = (ChoiceResponse) response;
            return cr.variants.get(id - 1);
        }
        if (response instanceof SearchResult) {
            Variant variant = new Variant();
            variant.searchResult = (SearchResult) response;
            return variant;
        }
        throw new BoxalinoException("no variant provided in choice response for variant id " + id);
    }

    @SuppressWarnings("unchecked")
    public Variant getChoiceResponseVariant(String choice, int count) throws BoxalinoException, Exception {
        int k = 0;
        for (BxRequest bxRequest : (ArrayList<BxRequest>) this.bxRequests) {
            k++;

            if (choice == null) {
                if (count > 0) {
                    count--;
                    continue;
                }
                return this.getChoiceIdResponseVariant(k);
            } else if (choice.equals("")) {
                if (count > 0) {
                    count--;
                    continue;
                }
                return this.getChoiceIdResponseVariant(k);
            } else if (choice.equals(bxRequest.getChoiceId())) {
                if (count > 0) {
                    count--;
                    continue;
                }
                return this.getChoiceIdResponseVariant(k);
            }

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public BxFacets getRequestFacets(String choice) {
        if (choice.isEmpty()) {
            if (((ArrayList<BxRequest>) this.bxRequests).get(0) != null) {
                return ((ArrayList<BxRequest>) this.bxRequests).get(0).getFacets();
            }
            return null;
        }
        for (BxRequest bxRequest : (ArrayList<BxRequest>) this.bxRequests) {
            if (bxRequest.getChoiceId().equals(choice)) {
                return bxRequest.getFacets();
            }
        }
        return null;
    }

    public SearchResult getFirstPositiveSuggestionSearchResult(Variant variant, int maxDistance) {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end

        if (variant.searchRelaxation.suggestionsResults == null) {
            return null;
        }
        for (SearchResult searchResult : variant.searchRelaxation.suggestionsResults) {
            if (searchResult.totalHitCount > 0) {
                if (searchResult.queryText.isEmpty() || variant.searchResult.queryText.isEmpty()) {
                    continue;
                }
                int distance = new LevenshteinDistance(searchResult.queryText, variant.searchResult.queryText).getSimilarity();
                if (distance <= maxDistance && distance != -1) {
                    return searchResult;
                }
            }
        }
        return null;
    }

    public SearchResult getVariantSearchResult(Variant variant, boolean considerRelaxation, int maxDistance) {

        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end

        SearchResult searchResult = variant.searchResult;
        if (considerRelaxation && variant.searchResult.totalHitCount == 0) {
            return this.getFirstPositiveSuggestionSearchResult(variant, maxDistance);
        }
        return searchResult;
    }

    public BxFacets getFacets(String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
        SearchResult searchResult = this.getVariantSearchResult(variant, considerRelaxation, maxDistance);
        BxFacets facets = this.getRequestFacets(choice);
        if (facets == null || searchResult == null) {
            return null;
        }
        facets.setFacetResponse(searchResult.facetResponses);
        return facets;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, List<String>>> getSearchHitFieldValues(SearchResult searchResult, List<String> fields) {
    	Map<String, Map<String, List<String>>> fieldValues = new LinkedHashMap<>();

        if (searchResult != null) {
            for (Hit item : searchResult.hits) {
            	String id = item.values.get("id").get(0).toString();
                List<String> finalFields = fields;
                if (finalFields == null) {
                     finalFields = Arrays.asList(item.values.keySet().toArray(new String[item.values.size()]));
                }
                
                fieldValues.put(id, new LinkedHashMap<>());
                for (String field : finalFields) {
                    if (item.values.get(field) != null) {
                        if (item.values.get(field).size() > 0) {
                            if (!item.values.get(field).get(0).equals(EMPTY_STRING) && item.values.get(field).get(0) != null) {
                            	fieldValues.get(id).put(field, item.values.get(field));
                            }
                        }
                    }
                }
            }
        }
        
        return fieldValues;
    }

    public Map<String, Map<String, List<String>>> getHitFieldValues(List<String> fields, String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
        return this.getSearchHitFieldValues(this.getVariantSearchResult(variant, considerRelaxation, maxDistance), fields);
    }

    @SuppressWarnings("unchecked")
    public Object getFirstHitFieldValue(String field, boolean returnOneValue, int hitIndex, String choice, int count, int maxDistance) throws Exception, BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default endException

        List<String> fieldNames = null;
        if (field != null) {
             fieldNames = java.util.Arrays.asList(field); 
        }
        count = 0;
        for (Map.Entry<String, Map<String, List<String>>> fieldValueMap : this.getHitFieldValues(fieldNames, choice, true, count, maxDistance).entrySet()) {
            Map<String, List<String>> temp_fieldValueMap = new HashMap<>();
            if (count++ < hitIndex) {
                continue;
            }
            for (Object fieldValues : temp_fieldValueMap.values()) {
                List<String> temp_fieldValues = new ArrayList<>();
                temp_fieldValues = (ArrayList<String>) fieldValues;
                if (temp_fieldValues.size() > 0) {
                    if (returnOneValue) {
                        return temp_fieldValues.get(0);
                    } else {
                        return temp_fieldValues;
                    }
                }
            }
        }
        return null;

    }

    public boolean areThereSubPhrases(String choice, int count) throws BoxalinoException {
        try {
            Variant variant = getChoiceResponseVariant(choice, count);
            return (variant.searchRelaxation.subphrasesResults) != null ? true : false && (variant.searchRelaxation.subphrasesResults).size() > 0;
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
    }

    public List< SearchResult> getSubPhrasesQueries(String choice, int count) throws BoxalinoException {

        if (!this.areThereSubPhrases(choice, count)) {
            return new ArrayList<>();
        }

        List<SearchResult> queries = new ArrayList<>();
        try {
            Variant variant = this.getChoiceResponseVariant(choice, count);
            for (SearchResult searchResult : variant.searchRelaxation.subphrasesResults) {
                queries.add(searchResult);
            }

            return queries;
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
    }

    protected SearchResult getSubPhraseSearchResult(String queryText, String choice, int count) throws BoxalinoException {
        if (!this.areThereSubPhrases(choice, count)) {
            return null;
        }
        try {
            Variant variant = this.getChoiceResponseVariant(choice, count);
            for (SearchResult searchResult : variant.searchRelaxation.subphrasesResults) {
                if (searchResult.queryText.equals(queryText)) {
                    return searchResult;
                }
            }

            return null;
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
    }

    public long getSubPhraseTotalHitCount(String queryText, String choice, int count) throws BoxalinoException {

        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return searchResult.totalHitCount;
        }
        return 0;
    }

    public List<String> getSearchResultHitIds(SearchResult searchResult, String fieldId) {
        //default start
        if (fieldId.equals(EMPTY_STRING)) {
            fieldId = "id";
        }
        //default end

        List<String> ids = new ArrayList<>();

        if (searchResult != null) {
            if (searchResult.hits.size() > 0) {
                for (Hit item : searchResult.hits) {
                    ids.add(((List<String>) item.values.get(fieldId)).get(0));
                }
            } else if ((searchResult.hitsGroups).size() > 0) {
                for (HitsGroup hitGroup : searchResult.hitsGroups) {
                    ids.add(hitGroup.groupValue);
                }
            }
        }
        return ids;
    }

    public List<String> getSubPhraseHitIds(String queryText, String choice, int count, String fieldId) throws BoxalinoException {
        //default start
        if (fieldId.equals(EMPTY_STRING)) {
            fieldId = "id";
        }
        //default end 
        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return this.getSearchResultHitIds(searchResult, fieldId);
        }
        return new ArrayList<>();
    }

    public List<String> getHitIds(String choice, boolean considerRelaxation, int count, int maxDistance, String fieldId) throws BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        if (fieldId.equals(EMPTY_STRING)) {
            fieldId = "id";
        }
        //default end 
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
        return this.getSearchResultHitIds(this.getVariantSearchResult(variant, considerRelaxation, maxDistance), fieldId);
    }

    public long getTotalHitCount(String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }

        //default end 
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }

        SearchResult searchResult = this.getVariantSearchResult(variant, considerRelaxation, maxDistance);
        if (searchResult == null) {
            return 0;
        }
        return searchResult.totalHitCount;
    }

    public Map<String, Map<String, List<String>>> getSubPhraseHitFieldValues(String queryText, List<String> fields, String choice, boolean considerRelaxation, int count) throws BoxalinoException, Exception {
        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return this.getSearchHitFieldValues(searchResult, fields);
        }
        return new LinkedHashMap<>();
    }

    public boolean areResultsCorrected(String choice, int count, int maxDistance) throws BoxalinoException {

        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end 
        return Boolean.FALSE.equals(this.getTotalHitCount(choice, false, count, 0) == 0 && this.getTotalHitCount(choice, true, count, maxDistance) > 0 && this.areThereSubPhrases(EMPTY_STRING, 0));
    }

    public String getCorrectedQuery(String choice, int count) throws BoxalinoException {
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
            SearchResult searchResult = this.getVariantSearchResult(variant, false, 0);
            if (searchResult != null) {
                return searchResult.queryText;
            }
        } catch (Exception ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String toJson(List<String>fields) throws BoxalinoException {
        List<Map<String, Object>> temp = new ArrayList<>();
        Map<String, Object> myObject = new HashMap<String, Object>();

        for (Map.Entry<String, Map<String, List<String>>> fieldValueMap : this.getHitFieldValues(fields, null, true, 0, 10).entrySet()) {
            Map<String, Object> hitFieldValues = new HashMap<String, Object>();
            fieldValueMap.getValue().entrySet().forEach((fieldValues) -> {
                hitFieldValues.put((fieldValues.getKey()), (new HashMap<String, Object>() {
                    {
                        put("values", fieldValues.getValue());
                    }
                }));
            });

            Map<String, Object> idMap = new HashMap<String, Object>() {
                {
                    put("id", fieldValueMap.getKey());
                }
            };
            Map<String, Object> fieldValues = new HashMap<String, Object>() {
                {
                    put("fieldValues", hitFieldValues);
                }
            };
            temp.add(idMap);
            temp.add(fieldValues);
        }

        myObject.put("hits", temp);
        return (myObject.toString());

    }

}
