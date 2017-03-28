/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import com.boxalino.p13n.api.thrift.AutocompleteQuery;
import com.boxalino.p13n.api.thrift.AutocompleteRequest;
import com.boxalino.p13n.api.thrift.PropertyQuery;
import com.boxalino.p13n.api.thrift.UserRecord;
import java.util.ArrayList;

/**
 *
 * @author HASHIR
 */
public class BxAutocompleteRequest {

    protected String language;
    protected String queryText;
    protected String choiceId;
    protected int textualSuggestionsHitCount;
    protected BxSearchRequest bxSearchRequest;
    protected String indexId = null;
    private ArrayList<PropertyQuery> propertyQueries = new ArrayList<>();

    public BxAutocompleteRequest(String language, String queryText, int textualSuggestionsHitCount, int productSuggestionHitCount, String autocompleteChoiceId, String searchChoiceId) throws BoxalinoException {
        //default paramter start
        if (productSuggestionHitCount==0) {
            productSuggestionHitCount = 5;
        }
        if ((autocompleteChoiceId == null) || (autocompleteChoiceId.isEmpty())) {
            autocompleteChoiceId = "autocomplete";
        }
        if ((searchChoiceId == null) || (searchChoiceId.isEmpty())) {
            searchChoiceId = "search";
        }
        
        //default parameter end    
        this.language = language;
        this.queryText = queryText;
        this.textualSuggestionsHitCount = textualSuggestionsHitCount;

        this.choiceId = autocompleteChoiceId;
        this.bxSearchRequest = new BxSearchRequest(language, queryText, productSuggestionHitCount, searchChoiceId);
    }

    public BxSearchRequest getBxSearchRequest() {
        return this.bxSearchRequest;
    }

    public void setBxSearchRequest(BxSearchRequest bxSearchRequest) {
        this.bxSearchRequest = bxSearchRequest;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getQuerytext() {
        return this.queryText;
    }

    public void setQuerytext(String queryText) {
        this.queryText = queryText;
    }

    public String getChoiceId() {
        return this.choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public int getTextualSuggestionHitCount() {
        return this.textualSuggestionsHitCount;
    }

    public void setTextualSuggestionHitCount(int textualSuggestionsHitCount) {
        this.textualSuggestionsHitCount = textualSuggestionsHitCount;
    }

    public String getIndexId() {
        return this.indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public void setDefaultIndexId(String indexId) {
        if (this.indexId == null) {
            this.setIndexId(indexId);
        }
        this.bxSearchRequest.setDefaultIndexId(indexId);
    }

    private AutocompleteQuery getAutocompleteQuery() {
        AutocompleteQuery autocompleteQuery = new AutocompleteQuery();
        autocompleteQuery.indexId = this.getIndexId();
        autocompleteQuery.language = this.language;
        autocompleteQuery.queryText = this.queryText;
        autocompleteQuery.suggestionsHitCount = this.textualSuggestionsHitCount;
        autocompleteQuery.highlight = true;
        autocompleteQuery.highlightPre = "<em>";
        autocompleteQuery.highlightPost = "</em>";
        return autocompleteQuery;

    }

    public void addPropertyQuery(String field, int hitCount, boolean evaluateTotal) {
        PropertyQuery propertyQuery = new PropertyQuery();
        propertyQuery.name = field;
        propertyQuery.hitCount = hitCount;
        propertyQuery.evaluateTotal = evaluateTotal;
        this.propertyQueries.add(propertyQuery);
    }

    public void resetPropertyQueries() {
        this.propertyQueries = new ArrayList<>();
    }
    
     public AutocompleteRequest getAutocompleteThriftRequest(String profileid, UserRecord thriftUserRecord)
        {
            AutocompleteRequest autocompleteRequest = new AutocompleteRequest();
            autocompleteRequest.userRecord = thriftUserRecord;
            autocompleteRequest.profileId = profileid;
            autocompleteRequest.choiceId = this.choiceId;
            autocompleteRequest.searchQuery = this.bxSearchRequest.getSimpleSearchQuery();
            autocompleteRequest.searchChoiceId = this.bxSearchRequest.getChoiceId();
            autocompleteRequest.autocompleteQuery = this.getAutocompleteQuery();

            if (this.propertyQueries.size() > 0)
            {
                autocompleteRequest.propertyQueries = this.propertyQueries;
            }

            return autocompleteRequest;
        }

}
