/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.RecommendationsSimilar;
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
public class RecommendationsSimilarTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public RecommendationsSimilarTest() {
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
    public void testFrontendRecommendationsSimilar() {
        RecommendationsSimilar _recommendationsSimilar = new RecommendationsSimilar();
        try {
            _recommendationsSimilar.account = this.account;
            _recommendationsSimilar.password = this.password;
            _recommendationsSimilar.print = false;
            List<String> hitIds= Arrays.asList("1","2","3","4","5","6","7","8","9","10");

            _recommendationsSimilar.recommendationsSimilar();
            assertEquals(Arrays.asList(_recommendationsSimilar.bxResponse.getHitIds("", true, 0, 10, "id").toArray(new String[0])), hitIds);

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
