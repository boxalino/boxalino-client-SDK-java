/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchReturnFields;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SearchReturnFieldsTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchReturnFieldsTest() {
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
    public void testFrontendSearchReturnFields() {
        SearchReturnFields _searchReturnFields = new SearchReturnFields();
        try {
            _searchReturnFields.account = this.account;
            _searchReturnFields.password = this.password;
            _searchReturnFields.print = false;

            _searchReturnFields.searchReturnFields();

            Map<String, Object> productColor1 = new HashMap<>();
            Map<String, Object> productColor2 = new HashMap<>();
            
            ArrayList<String> color1 = new ArrayList<String>();
            color1.add("Black");
            color1.add("Gray");
            color1.add("Yellow");
            
            ArrayList<String> color2 = new ArrayList<String>();
            color2.add("Gray");
            color2.add("Orange");
            color2.add("Yellow");
            
            productColor1.put("products_color", color1);
            productColor2.put("products_color", color2);
            
            ArrayList<String> fieldNames = new ArrayList<String>();            
            fieldNames.add("products_color");
            for (Map.Entry item : _searchReturnFields.bxResponse.getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).entrySet()) {
                if (item.getKey().toString().equals("41")) {
                    assertEquals(item.getValue(), productColor1);
                }
                if (item.getKey().toString().equals("1940")) {
                    assertEquals(item.getValue(), productColor2);
                }

            }
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
