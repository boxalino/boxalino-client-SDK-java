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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

/**
 *
 * @author HASHIR
 */
public class BxChooseResponse {

    private Object response;
    private Object bxRequests;

    public BxChooseResponse(ChoiceResponse response, ArrayList<BxRequest> bxRequests) {
        this.response = response;
        if (bxRequests == null) {
            bxRequests = new ArrayList<BxRequest>();
        }
        this.bxRequests = bxRequests;
    }

    public BxChooseResponse(SearchResult response, List<BxSearchRequest> bxRequests) {
        this.response = response;
        if (bxRequests == null) {
            bxRequests = new ArrayList<BxSearchRequest>();
        }
        this.bxRequests = bxRequests;
    }

    public Object getResponse() {
        return this.response;
    }

    protected Variant getChoiceIdResponseVariant(int id) throws BoxalinoException, NoSuchFieldException {
        Object response = this.getResponse();
        if (response.getClass().getDeclaredField("Variants") != null) {
            if (((ChoiceResponse) response).variants != null) {
                return ((ChoiceResponse) response).variants.get(id - 1);
            }
        }

        if (response.getClass().getName() == (new SearchResult().getClass().getName())) {
            Variant variant = new Variant();
            variant.searchResult = (SearchResult) response;
            return variant;
        }
        throw new BoxalinoException("no variant provided in choice response for variant id " + id);
    }

    public Variant getChoiceResponseVariant(String choice, int count) throws BoxalinoException, NoSuchFieldException {
        int k = 0;
        for (BxRequest bxRequest : (ArrayList<BxRequest>) this.bxRequests) {
            k++;
            if (choice == null || choice == bxRequest.getChoiceId()) {
                if (count > 0) {
                    count--;
                    continue;
                }
                return this.getChoiceIdResponseVariant(k);
            }
        }
        return null;
    }

    public List<Map<String, Object>> retrieveHitFieldValues(Hit item, String field, List<Hit> fields, String[] hits) {
        List<Map<String, Object>> fieldValues = new ArrayList<Map<String, Object>>();
        for (BxRequest bxRequest : (ArrayList<BxRequest>) this.bxRequests) {
            fieldValues.addAll(bxRequest.retrieveHitFieldValues(item, field, fields, hits));
        }
        return fieldValues;
    }

    public BxFacets getRequestFacets(String choice) {
        if (choice == null) {
            if (((ArrayList<BxRequest>) this.bxRequests).get(0) != null) {
                return ((ArrayList<BxRequest>) this.bxRequests).get(0).getFacets();
            }
            return null;
        }
        for (BxRequest bxRequest : (ArrayList<BxRequest>) this.bxRequests) {
            if (bxRequest.getChoiceId() == choice) {
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
                if (searchResult.queryText == "" || variant.searchResult.queryText == "") {
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

    public BxFacets getFacets(String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException, NoSuchFieldException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end

        Variant variant = this.getChoiceResponseVariant(choice, count);
        SearchResult searchResult = this.getVariantSearchResult(variant, considerRelaxation, maxDistance);
        BxFacets facets = this.getRequestFacets(choice);
        if (facets == null || searchResult == null) {
            return null;
        }
        facets.setFacetResponse(searchResult.facetResponses);
        return facets;
    }

    public Map<String, Object> getSearchHitFieldValues(SearchResult searchResult, String[] fields) {
        Map<String, Object> fieldValues = new HashMap<String, Object>();

        if (searchResult != null) {
            for (Hit item : searchResult.hits) {
                String[] finalFields = fields;
                if (finalFields == null) {
                    finalFields = item.values.keySet().toArray(new String[item.values.size()]);
                }

                for (String field : finalFields) {
                    if (item.values.get(field) != null) {
                        if (item.values.get(field).get(0) != EMPTY_STRING || item.values.get(field).get(0) != null) {

                            ((HashMap) fieldValues.get(((ArrayList<String>) item.values.get("id")).get(0))).put(field, item.values.get(field));

                        }
                    }
                    if (((HashMap) fieldValues.get(((ArrayList<String>) item.values.get("id")).get(0))).get(field) == null) {
                        ((HashMap) fieldValues.get(((ArrayList<String>) item.values.get("id")).get(0))).put(field, this.retrieveHitFieldValues(item, field, searchResult.hits, finalFields));

                    }

                }

            }
        }
        return fieldValues;
    }

    public Map<String, Object> getHitFieldValues(String[] fields, String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException, NoSuchFieldException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end
        Variant variant = this.getChoiceResponseVariant(choice, count);
        return this.getSearchHitFieldValues(this.getVariantSearchResult(variant, considerRelaxation, maxDistance), fields);
    }

    public Object getFirstHitFieldValue(String field, boolean returnOneValue, int hitIndex, String choice, int count, int maxDistance) throws NoSuchFieldException, BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end

        String[] fieldNames = null;
        if (field != null) {
            fieldNames = new String[]{field};
        }
        count = 0;
        for (Map.Entry<String, Object> fieldValueMap : this.getHitFieldValues(fieldNames, choice, true, count, maxDistance).entrySet()) {
            Map<String, List<String>> temp_fieldValueMap = new HashMap<String, List<String>>();
            if (count++ < hitIndex) {
                continue;
            }
            for (Object fieldValues : temp_fieldValueMap.values()) {
                List<String> temp_fieldValues = new ArrayList<String>();
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

    public boolean areThereSubPhrases(String choice, int count) throws BoxalinoException, NoSuchFieldException {
        Variant variant = getChoiceResponseVariant(choice, count);
        return (variant.searchRelaxation.subphrasesResults) != null ? true : false && (variant.searchRelaxation.subphrasesResults).size() > 0;
    }

    public Map<String, SearchResult> getSubPhrasesQueries(String choice, int count) throws BoxalinoException, NoSuchFieldException {

        if (!this.areThereSubPhrases(choice, count)) {
            return new HashMap<String, SearchResult>();
        }

        Map<String, SearchResult> queries = new HashMap<String, SearchResult>();
        Variant variant = this.getChoiceResponseVariant(choice, count);
        int index = 0;
        for (SearchResult searchResult : variant.searchRelaxation.subphrasesResults) {
            queries.put(String.valueOf(index++), searchResult);
        }

        return queries;
    }

    protected SearchResult getSubPhraseSearchResult(String queryText, String choice, int count) throws BoxalinoException, NoSuchFieldException {
        if (!this.areThereSubPhrases(choice, count)) {
            return null;
        }
        Variant variant = this.getChoiceResponseVariant(choice, count);
        for (SearchResult searchResult : variant.searchRelaxation.subphrasesResults) {
            if (searchResult.queryText == queryText) {
                return searchResult;
            }
        }

        return null;
    }

    public long getSubPhraseTotalHitCount(String queryText, String choice, int count) throws BoxalinoException, NoSuchFieldException {

        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return searchResult.totalHitCount;
        }
        return 0;
    }

    public Map<String, String> getSearchResultHitIds(SearchResult searchResult, String fieldId) {
        //default start
        if (fieldId == EMPTY_STRING) {
            fieldId = "id";
        }
        //default end

        Map<String, String> ids = new HashMap<String, String>();

        if (searchResult != null) {
            if (searchResult.hits.size() > 0) {
                int index = 0;
                for (Hit item : searchResult.hits) {
                    ids.put(String.valueOf(index++), ((List<String>) item.values.get(fieldId)).get(0));
                }
            } else if ((searchResult.hitsGroups).size() > 0) {
                int index = 0;
                for (HitsGroup hitGroup : searchResult.hitsGroups) {
                    ids.put(String.valueOf(index++), hitGroup.groupValue);
                }
            }
        }
        return ids;
    }

    public Map<String, String> getSubPhraseHitIds(String queryText, String choice, int count, String fieldId) throws BoxalinoException, NoSuchFieldException {
        //default start
        if (fieldId == EMPTY_STRING) {
            fieldId = "id";
        }
        //default end 
        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return this.getSearchResultHitIds(searchResult, fieldId);
        }
        return new HashMap<String, String>();
    }

    public Map<String, String> getHitIds(String choice, boolean considerRelaxation, int count, int maxDistance, String fieldId) throws BoxalinoException, NoSuchFieldException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        if (fieldId == EMPTY_STRING) {
            fieldId = "id";
        }
        //default end 
        Variant variant = this.getChoiceResponseVariant(choice, count);
        return this.getSearchResultHitIds(this.getVariantSearchResult(variant, considerRelaxation, maxDistance), fieldId);
    }

    public long getTotalHitCount(String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException, NoSuchFieldException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }

        //default end 
        Variant variant = this.getChoiceResponseVariant(choice, count);
        SearchResult searchResult = this.getVariantSearchResult(variant, considerRelaxation, maxDistance);
        if (searchResult == null) {
            return 0;
        }
        return searchResult.totalHitCount;
    }

    public Map<String, Object> getSubPhraseHitFieldValues(String queryText, String[] fields, String choice, boolean considerRelaxation, int count) throws BoxalinoException, NoSuchFieldException {
        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return this.getSearchHitFieldValues(searchResult, fields);
        }
        return new HashMap<String, Object>();
    }

    public boolean areResultsCorrected(String choice, int count, int maxDistance) throws BoxalinoException, NoSuchFieldException {

        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end 
        return this.getTotalHitCount(choice, false, count, 0) == 0 && this.getTotalHitCount(choice, true, count, maxDistance) > 0 && this.areThereSubPhrases(EMPTY_STRING, 0) == false;
    }

    public String getCorrectedQuery(String choice, int count) throws BoxalinoException, NoSuchFieldException {
        Variant variant = this.getChoiceResponseVariant(choice, count);
        SearchResult searchResult = this.getVariantSearchResult(variant, false, 0);
        if (searchResult != null) {
            return searchResult.queryText;
        }
        return null;
    }

    public String toJson(String[] fields) throws BoxalinoException, NoSuchFieldException {
        List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
        Map<String, Object> myObject = new HashMap<String, Object>();
        for (Map.Entry<String, Object> fieldValueMap : this.getHitFieldValues(fields, null, true, 0, 10).entrySet()) {
            Map<String, Object> hitFieldValues = new HashMap<String, Object>();
            for (Map.Entry<String, Object> fieldValues : ((Map<String, Object>) fieldValueMap.getValue()).entrySet()) {
                hitFieldValues.put((fieldValues.getKey()), (new HashMap<String, Object>() {
                    {
                        put("values", fieldValues.getValue());
                    }
                }));
            }

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
        return JSONArray.fromObject(myObject).toString();

    }

}
