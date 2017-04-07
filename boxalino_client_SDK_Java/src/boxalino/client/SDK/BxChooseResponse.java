/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import static Helper.Common.EMPTY_STRING;
import Helper.LevenshteinDistance;
import com.boxalino.p13n.api.thrift.ChoiceResponse;
import com.boxalino.p13n.api.thrift.Hit;
import com.boxalino.p13n.api.thrift.HitsGroup;
import com.boxalino.p13n.api.thrift.SearchResult;
import com.boxalino.p13n.api.thrift.Variant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

    protected Variant getChoiceIdResponseVariant(int id) throws BoxalinoException, NoSuchFieldException {
        Object responsee = this.getResponse();
        Field field = null;
        try {
            field = responsee.getClass().getField("variants");
        } catch (NoSuchFieldException ex) {
        }

        if (field != null) {
            if (((ChoiceResponse) responsee).variants != null) {
                return ((ChoiceResponse) responsee).variants.get(id - 1);
            }
        }

        if (responsee.getClass().getName().equals(new SearchResult().getClass().getName())) {
            Variant variant = new Variant();
            variant.searchResult = (SearchResult) responsee;
            return variant;
        }
        throw new BoxalinoException("no variant provided in choice response for variant id " + id);
    }

    public Variant getChoiceResponseVariant(String choice, int count) throws BoxalinoException, NoSuchFieldException {
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

    public List<Map<String, Object>> retrieveHitFieldValues(Hit item, String field, List<Hit> fields, String[] hits) {
        List<Map<String, Object>> fieldValues = new ArrayList<>();
        ((ArrayList<BxRequest>) this.bxRequests).forEach((bxRequest) -> {
            fieldValues.addAll(bxRequest.retrieveHitFieldValues(item, field, fields, hits));
        });
        return fieldValues;
    }

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
        } catch (NoSuchFieldException ex) {
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

    public Map<String, Object> getSearchHitFieldValues(SearchResult searchResult, String[] fields) {
        Map<String, Object> fieldValues = new HashMap<>();

        if (searchResult != null) {
            for (Hit item : searchResult.hits) {
                String[] finalFields = fields;
                if (finalFields == null) {
                    finalFields = item.values.keySet().toArray(new String[item.values.size()]);
                }

                for (String field : finalFields) {
                    if (item.values.get(field) != null) {
                        if (item.values.get(field).size() > 0) {
                            if (!item.values.get(field).get(0).equals(EMPTY_STRING) || item.values.get(field).get(0) != null) {
                                String key = item.values.get("id").get(0).toString();
                                fieldValues.put(key, new HashMap());
                                ((HashMap) fieldValues.get(key)).put(field, item.values.get(field));
                            }
                        }
                    }

                    if (fieldValues.get(((ArrayList<String>) item.values.get("id")).get(0)) == null) {
                        fieldValues.put(((ArrayList<String>) item.values.get("id")).get(0), new HashMap());
                        ((HashMap) fieldValues.get(((ArrayList<String>) item.values.get("id")).get(0))).put(field, this.retrieveHitFieldValues(item, field, searchResult.hits, finalFields));

                    }

                }
            }
        }
        return fieldValues;
    }

    public Map<String, Object> getHitFieldValues(String[] fields, String choice, boolean considerRelaxation, int count, int maxDistance) throws BoxalinoException {
        //default start
        if (maxDistance == 0) {
            maxDistance = 10;
        }
        //default end
        Variant variant = null;
        try {
            variant = this.getChoiceResponseVariant(choice, count);
        } catch (NoSuchFieldException ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
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
        } catch (NoSuchFieldException ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
    }

    public Map<String, SearchResult> getSubPhrasesQueries(String choice, int count) throws BoxalinoException {

        if (!this.areThereSubPhrases(choice, count)) {
            return new HashMap<>();
        }

        Map<String, SearchResult> queries = new HashMap<>();
        try {
            Variant variant = this.getChoiceResponseVariant(choice, count);
            int index = 0;
            for (SearchResult searchResult : variant.searchRelaxation.subphrasesResults) {
                queries.put(String.valueOf(index++), searchResult);
            }

            return queries;
        } catch (NoSuchFieldException ex) {
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
        } catch (NoSuchFieldException ex) {
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

    public Map<String, String> getSearchResultHitIds(SearchResult searchResult, String fieldId) {
        //default start
        if (fieldId.equals(EMPTY_STRING)) {
            fieldId = "id";
        }
        //default end

        Map<String, String> ids = new HashMap<>();

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

    public Map<String, String> getSubPhraseHitIds(String queryText, String choice, int count, String fieldId) throws BoxalinoException {
        //default start
        if (fieldId.equals(EMPTY_STRING)) {
            fieldId = "id";
        }
        //default end 
        SearchResult searchResult = this.getSubPhraseSearchResult(queryText, choice, count);
        if (searchResult != null) {
            return this.getSearchResultHitIds(searchResult, fieldId);
        }
        return new HashMap<>();
    }

    public Map<String, String> getHitIds(String choice, boolean considerRelaxation, int count, int maxDistance, String fieldId) throws BoxalinoException {
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
        } catch (NoSuchFieldException ex) {
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
        } catch (NoSuchFieldException ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }

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
        return new HashMap<>();
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
        } catch (NoSuchFieldException ex) {
            throw new BoxalinoException(ex.getMessage(), ex.getCause());
        }
        return null;
    }

    public String toJson(String[] fields) throws BoxalinoException {
        List<Map<String, Object>> temp = new ArrayList<>();
        Map<String, Object> myObject = new HashMap<String, Object>();

        for (Map.Entry<String, Object> fieldValueMap : this.getHitFieldValues(fields, null, true, 0, 10).entrySet()) {
            Map<String, Object> hitFieldValues = new HashMap<String, Object>();
            ((Map<String, Object>) fieldValueMap.getValue()).entrySet().forEach((fieldValues) -> {
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
