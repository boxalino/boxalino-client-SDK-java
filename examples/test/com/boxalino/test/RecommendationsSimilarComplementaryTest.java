/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.RecommendationsSimilarComplementary;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
            
            String[] complementaryIds = new String[10];
            String[] similarIds = new String[10];
            int j=11;
            for (int i = 0; i < 10; i++) 
            {
                complementaryIds[i] = String.valueOf(j);
                similarIds[i]= String.valueOf(i+1);
                j++;
            }

           
            _recommendationsSimilarComplementary.recommendationsSimilarComplementary();
            assertEquals(_recommendationsSimilarComplementary.bxResponse.getHitIds(choiceIdSimilar, true, 0, 10, "id").values().toArray(new String[0]), similarIds);
            assertEquals(_recommendationsSimilarComplementary.bxResponse.getHitIds(choiceIdComplementary, true, 0, 10, "id").values().toArray(new String[0]), complementaryIds);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
