/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.ChoiceResponse;
import java.util.ArrayList;

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
}
