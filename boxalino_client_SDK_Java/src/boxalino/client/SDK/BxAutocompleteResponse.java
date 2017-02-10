/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import Exception.BoxalinoException;
import Helper.Common;
import com.boxalino.p13n.api.thrift.AutocompleteHit;
import com.boxalino.p13n.api.thrift.AutocompleteResponse;
import com.boxalino.p13n.api.thrift.PropertyHit;
import com.boxalino.p13n.api.thrift.PropertyResult;
import com.boxalino.p13n.api.thrift.SearchResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HASHIR
 */
public class BxAutocompleteResponse {

    private AutocompleteResponse response;
    private BxAutocompleteRequest bxAutocompleteRequest;

    public BxAutocompleteResponse(AutocompleteResponse response, BxAutocompleteRequest bxAutocompleteRequest) {
        this.response = response;
        this.bxAutocompleteRequest = bxAutocompleteRequest;
    }

    public AutocompleteResponse getResponse() {
        return this.response;
    }

    public String getPrefixSearchHash() {
        if (this.getResponse().prefixSearchResult.totalHitCount > 0) {
            return Common.MD5(this.getResponse().prefixSearchResult.queryText).substring(0, 10);
        } else {
            return null;
        }
    }

    public List<String> getTextualSuggestions() {
        List<String> suggestions = new ArrayList<String>();

        for (AutocompleteHit hit : this.getResponse().hits) {
            suggestions.add(hit.suggestion);
        }
        return suggestions;
    }

    protected AutocompleteHit getTextualSuggestionHit(String suggestion) throws BoxalinoException {
        for (AutocompleteHit hit : this.getResponse().hits) {
            if (hit.suggestion == suggestion) {
                return hit;
            }
        }
        throw new BoxalinoException("unexisting textual suggestion provided " + suggestion);
    }

    public long getTextualSuggestionTotalHitCount(String suggestion) throws BoxalinoException {
        AutocompleteHit hit = this.getTextualSuggestionHit(suggestion);
        return hit.searchResult.totalHitCount;
    }

    public BxChooseResponse getBxSearchResponse(String textualSuggestion) throws BoxalinoException {
        BxAutocompleteRequest bxAutocompleteRequest = this.bxAutocompleteRequest;
        SearchResult searchResult = textualSuggestion == null ? this.getResponse().prefixSearchResult : this.getTextualSuggestionHit(textualSuggestion).searchResult;
        ArrayList<BxSearchRequest> bxRequests = new ArrayList() {
            {
                add(bxAutocompleteRequest.getBxSearchRequest());
            }
        };
        BxChooseResponse bxChooseResponse = new BxChooseResponse(searchResult, bxRequests);
        return bxChooseResponse;
    }

    public List<PropertyHit> getPropertyHits(String field) {

        for (PropertyResult propertyResult : this.getResponse().propertyResults) {
            if (propertyResult.name == field) {
                return (List<PropertyHit>) propertyResult.hits;
            }
        }
        return new ArrayList<PropertyHit>();
    }

    public PropertyHit getPropertyHit(String field, String hitValue) {
        for (PropertyHit hit : this.getPropertyHits(field)) {
            if (hit.value == hitValue) {
                return hit;
            }
        }
        return null;
    }

    public List<String> getPropertyHitValues(String field) {
        List<String> hitValues = new ArrayList<String>();
        for (PropertyHit hit : this.getPropertyHits(field)) {
            hitValues.add(hit.value);
        }
        return hitValues;
    }

    public String getPropertyHitValueLabel(String field, String hitValue) {
        PropertyHit hit = this.getPropertyHit(field, hitValue);
        if (hit != null) {
            return hit.label;
        }
        return null;
    }

    public long getPropertyHitValueTotalHitCount(String field, String hitValue) {
        PropertyHit hit = this.getPropertyHit(field, hitValue);
        if (hit != null) {
            return hit.totalHitCount;
        }
        return 0;
    }

}
