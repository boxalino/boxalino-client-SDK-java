/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchFacetPrice;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Convert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pc
 */
public class SearchFacetPriceTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchFacetPriceTest() {
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
    public void testFrontendSearchFacetPrice() {
        SearchFacetPrice _searchFacetPrice = new SearchFacetPrice();
        try {
            _searchFacetPrice.account = this.account;
            _searchFacetPrice.password = this.password;
            _searchFacetPrice.print = false;

            _searchFacetPrice.searchFacetPrice();
            List<String> priceRange = new ArrayList<String>();
            priceRange = Arrays.asList(_searchFacetPrice.facets.getPriceRanges().keySet().toArray(new String[0]));
            assertEquals(priceRange.get(0), "22.0-37.5");
            for (Map.Entry item : _searchFacetPrice.bxResponse.getHitFieldValues(new String[]{_searchFacetPrice.facets.getPriceFieldName()}, "", true, 0, 10).entrySet()) {

                for (Map.Entry fieldValueMapItem : ((Map<String, List<String>>) item.getValue()).entrySet()) {

                    List<String> facetList = (List<String>) fieldValueMapItem.getValue();
                    Double facetValue = Double.valueOf(facetList.get(0));
                    assertTrue(facetValue > 22.0);
                    assertTrue(facetValue <= 84.0);
                }
            }

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
