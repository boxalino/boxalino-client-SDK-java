/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.RecommendationsSimilarComplementary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pc
 */
public class RecommendationsSimilarComplementaryTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public RecommendationsSimilarComplementaryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testFrontendRecommendationsSimilarComplementary() {
        RecommendationsSimilarComplementary _recommendationsSimilarComplementary = new RecommendationsSimilarComplementary();
        try {
            _recommendationsSimilarComplementary.account = this.account;
            _recommendationsSimilarComplementary.password = this.password;
            _recommendationsSimilarComplementary.print = false;
            String choiceIdSimilar = "similar";
            String choiceIdComplementary = "complementary";
            
            List<String> complementaryIds = new ArrayList<String>();
            List<String> similarIds = new ArrayList<String>();
            int j=11;
            for (int i = 0; i < 10; i++) 
            {
                complementaryIds.add(String.valueOf(j));
                similarIds.add(String.valueOf(i+1));
                j++;
            }

           
            _recommendationsSimilarComplementary.recommendationsSimilarComplementary();
            assertEquals(Arrays.asList(_recommendationsSimilarComplementary.bxResponse.getHitIds(choiceIdSimilar, true, 0, 10, "id").toArray(new String[0])), similarIds);
            assertEquals(Arrays.asList(_recommendationsSimilarComplementary.bxResponse.getHitIds(choiceIdComplementary, true, 0, 10, "id").toArray(new String[0])), complementaryIds);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
