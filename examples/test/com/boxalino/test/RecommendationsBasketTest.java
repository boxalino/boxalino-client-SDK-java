/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.RecommendationsBasket;
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
public class RecommendationsBasketTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public RecommendationsBasketTest() {
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
    public void testFrontendRecommendationsBasket() {
        RecommendationsBasket _recommendationsBasket = new RecommendationsBasket();
        try {
            _recommendationsBasket._account = this.account;
            _recommendationsBasket._password = this.password;
            _recommendationsBasket._print = false;
           
            List<String> hitIds= Arrays.asList("1","2","3","4","5","6","7","8","9","10");
            
            _recommendationsBasket.recommendationsBasket();   
            
         
                    
                    
            
           assertEquals(Arrays.asList(_recommendationsBasket.bxResponse.getHitIds("", true, 0, 10, "id").toArray(new String[0])), hitIds);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
