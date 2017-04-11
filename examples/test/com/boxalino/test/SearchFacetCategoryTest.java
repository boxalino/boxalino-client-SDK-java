/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchFacetCategory;
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
public class SearchFacetCategoryTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchFacetCategoryTest() {
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
    public void testFrontendSearchFacetCategory() {
        SearchFacetCategory _searchFacetCategory = new SearchFacetCategory();
        try {
            _searchFacetCategory.account = this.account;
            _searchFacetCategory.password = this.password;
            _searchFacetCategory.print = false;

            _searchFacetCategory.searchFacetCategory();
            List<String> hitIds = Arrays.asList("41", "1940");
            assertEquals(Arrays.asList(_searchFacetCategory.bxResponse.getHitIds("", true, 0, 10, "id").values().toArray(new String[0])), hitIds);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
