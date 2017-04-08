/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchAutocompleteProperty;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pc
 */
public class SearchAutocompletePropertyTest {
    
    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";
    
    public SearchAutocompletePropertyTest() {
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
     public void testFrontendSearchAutocompleteProperty() {
     SearchAutocompleteProperty _searchAutocompleteProperty = new SearchAutocompleteProperty();
            try
            {
                _searchAutocompleteProperty.account = this.account;
                _searchAutocompleteProperty.password = this.password;
                _searchAutocompleteProperty.print = false;
                _searchAutocompleteProperty.searchAutocompleteProperty();
                List<String> propertyHitValues = _searchAutocompleteProperty.bxAutocompleteResponse.getPropertyHitValues("categories");

                assertEquals(propertyHitValues.size(), 2);
                assertEquals(propertyHitValues.get(0),"Bras &amp; Tanks" );
                assertEquals(propertyHitValues.get(1), "Hoodies &amp; Sweatshirts");
            }
            catch (Exception ex)
            {
                Assert.fail("Expected no exception, but got: " + ex.getMessage());
            }
     }
}
