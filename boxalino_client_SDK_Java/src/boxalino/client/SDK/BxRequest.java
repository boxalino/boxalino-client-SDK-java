/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import static Helper.Common.emptyIfNull;
import Helper.CustomBasketContent;
import Helper.Shift;
import com.boxalino.p13n.api.thrift.ContextItem;
import com.boxalino.p13n.api.thrift.Filter;
import com.boxalino.p13n.api.thrift.Hit;
import com.boxalino.p13n.api.thrift.SimpleSearchQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author HASHIR
 */
public class BxRequest {

    protected String indexId;
    protected Map<String, String> requestMap;
    protected ArrayList<String> returnFields;
    protected int offset;
    protected String queryText;
    protected BxFacets bxFacets;
    protected BxSortFields bxSortFields;
    protected Map<String, BxFilter> bxFilters;
    protected boolean orFilters;
    protected ArrayList<ContextItem> contextItems;

    private String language;
    private String choiceId;
    private float max;
    private float min;
    private boolean withRelaxation;
    private String groupBy;

    public BxRequest(String language, String choiceId, float max, float min) throws BoxalinoException {

        // default parameter start
        if (max == 0.0f) {
            max = 10;
        }
        if (min == 0.0f) {
            min = 0;
        }
        //default parameter end

        indexId = Helper.Common.EMPTY_STRING;
        requestMap = new HashMap<String, String>();
        returnFields = new ArrayList<String>();
        offset = 0;
        queryText = Helper.Common.EMPTY_STRING;
        bxFacets = new BxFacets();
        bxSortFields = new BxSortFields();
        bxFilters = new HashMap<>();
        orFilters = false;

        try {

            if (choiceId.isEmpty()) {
                throw new BoxalinoException("BxRequest created with null choiceId");
            }
            this.language = language;
            this.choiceId = choiceId;
            this.max = max;
            this.min = min;
            if (this.max == 0) {
                this.max = 1;
            }
            this.withRelaxation = choiceId == "search";
        } catch (BoxalinoException ex) {
           throw ex;
        }

    }

    public void setQuerytext(String queryText) {
        this.queryText = queryText;
    }

    public void setDefaultIndexId(String indexId) {
        if (indexId != null || indexId != Helper.Common.EMPTY_STRING) {
            this.setIndexId(indexId);
        }
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
        int k = 0;
        for (ContextItem contextItem : emptyIfNull(contextItems)) {
            if (contextItem.indexId == null || contextItem.indexId == Helper.Common.EMPTY_STRING) {
                this.contextItems.get(k).indexId = indexId;
            }
            k++;
        }
    }

    public String getIndexId() {
        return this.indexId;
    }

    public String getLanguage() {
        return this.language;
    }

    public ArrayList<String> getReturnFields() {
        return this.returnFields;
    }

    public int getOffset() {
        return this.offset;
    }

    public float getMax() {
        return this.max;
    }

    public String getQuerytext() {
        return this.queryText;
    }

    public BxFacets getFacets() {
        return this.bxFacets;
    }

    public Map<String, BxFilter> getFilters() {
        Map<String, BxFilter> filters = this.bxFilters;
        if (this.getFacets() != null) {
            for (Map.Entry filter : this.getFacets().getFilters().entrySet()) {
                filters.put(filter.getKey().toString(), (BxFilter) filter.getValue());
            }
        }
        return this.bxFilters;
    }

    public boolean getOrFilters() {
        return this.orFilters;
    }

    public BxSortFields getSortFields() {
        return this.bxSortFields;
    }

    public SimpleSearchQuery getSimpleSearchQuery() {
        SimpleSearchQuery searchQuery = new SimpleSearchQuery();
        searchQuery.indexId = this.getIndexId();
        searchQuery.language = this.getLanguage();
        searchQuery.returnFields = getReturnFields();
        searchQuery.offset = this.getOffset();
        searchQuery.hitCount = (int) this.getMax(); // converted into int
        searchQuery.queryText = this.getQuerytext();
        searchQuery.groupBy = this.groupBy;
        if (this.getFilters().size() > 0) {
            searchQuery.filters = new ArrayList<>();
            this.getFilters().entrySet().forEach((filter) -> {
                searchQuery.filters.add(((BxFilter) filter).getThriftFilter());
            });
        }
        searchQuery.orFilters = this.getOrFilters();
        if (this.getFacets() != null) {
            searchQuery.facetRequests = this.getFacets().getThriftFacets();
        }
        if (this.getSortFields() != null) {
            searchQuery.sortFields = this.getSortFields().getThriftSortFields();
        }
        return searchQuery;
    }

    public Map<String, ArrayList<String>> getRequestContextParameters() {

        Map<String, ArrayList<String>> contextParameters = new HashMap<>();
        return contextParameters;
    }

    public void setDefaultRequestMap(Map<String, String> requestMap) {
        if (this.requestMap == null) {
            this.requestMap = requestMap;
        }
    }

    public String getChoiceId() {
        return this.choiceId;
    }

    public ArrayList<ContextItem> getContextItems() {
        return this.contextItems;
    }

    public float getMin() {
        return this.min;
    }

    public boolean getWithRelaxation() {
        return this.withRelaxation;
    }

    public void setProductContext(String fieldName, String contextItemId, String role) {
        //default start
        role = "mainProduct";
        //default end
        ContextItem contextItem = new ContextItem();
        contextItem.indexId = this.getIndexId();
        contextItem.fieldName = fieldName;
        contextItem.contextItemId = contextItemId;
        contextItem.role = role;
        this.contextItems.add(contextItem);
    }

    public void setSortFields(BxSortFields bxSortFields) {
        this.bxSortFields = bxSortFields;
    }

    public void setFilters(Map<String, BxFilter> bxFilters) {
        this.bxFilters = bxFilters;
    }

    public void addFilter(BxFilter bxFilter) {
        this.bxFilters.put(bxFilter.getFieldName(), bxFilter);
    }

    public void setOrFilters(boolean orFilters) {
        this.orFilters = orFilters;
    }

    public void addSortField(String field, boolean reverse) {
        if (this.bxSortFields == null) {
            this.bxSortFields = new BxSortFields();
        }
        this.bxSortFields.push(field, reverse);
    }

    public void setReturnFields(ArrayList<String> returnFields) {
        this.returnFields = returnFields;
    }

    public void setFacets(BxFacets bxFacets) {
        this.bxFacets = bxFacets;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGroupBy() {
        return this.groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setBasketProductWithPrices(String fieldName, ArrayList<CustomBasketContent> basketContent, String role, String subRole) {
        //default start
        if (role == null || role == Helper.Common.EMPTY_STRING) {
            role = "mainProduct";
        }
        if (subRole == null || subRole == Helper.Common.EMPTY_STRING) {
            subRole = "mainProduct";
        }

        //default end
        if (basketContent != null && basketContent.size() > 0) {

            Collections.sort(basketContent, new Comparator<CustomBasketContent>() {
                @Override
                public int compare(CustomBasketContent a, CustomBasketContent b) {
                    return a.Price.compareTo(b.Price);
                }
            });
            List<CustomBasketContent> basketItem = new Shift<CustomBasketContent>().array_shift(basketContent);
            ContextItem contextItem = new ContextItem();
            contextItem.indexId = this.getIndexId();
            contextItem.fieldName = fieldName;
            contextItem.contextItemId = basketItem.get(0).Id;
            contextItem.role = role;
            this.contextItems.add(contextItem);

            for (CustomBasketContent basketItem1 : basketContent) {
                ContextItem contextItem1 = new ContextItem();
                contextItem1.indexId = this.getIndexId();
                contextItem1.fieldName = fieldName;
                contextItem1.contextItemId = basketItem1.Id;
                contextItem1.role = subRole;
                this.contextItems.add(contextItem1);
            }
        }
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setWithRelaxation(boolean withRelaxation) {
        this.withRelaxation = withRelaxation;
    }

    public List<Map<String, Object>> retrieveHitFieldValues(Hit item, String field, List<Hit> items, String[] fields) {
        return new ArrayList<>();
    }

}
