/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.AutocompleteResponse;

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

}
