/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

/**
 *
 * @author HASHIR
 */
public class BxRecommendationRequest extends BxRequest {

    public BxRecommendationRequest(String language, String choiceIdSimilar, int hitCount) {
        super(language, choiceIdSimilar, hitCount, 0);

    }
}
