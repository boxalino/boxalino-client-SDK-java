/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;
import com.boxalino.examples.Search2ndPage;
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
public class Search2ndPageTest {
    
    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";
    public Search2ndPageTest() {
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
     public void testFrontendSearch2ndPage() {
     Search2ndPage _search2ndPage = new Search2ndPage();
            try
            {
                _search2ndPage.account = this.account;
                _search2ndPage.password = this.password;
                _search2ndPage.print = false;
                List<String> hitIds= Arrays.asList("40","41","42","44");
                _search2ndPage.Search2ndPage();
                assertEquals(Arrays.asList(_search2ndPage.bxResponse.getHitIds("", true, 0, 10, "id").values().toArray(new String[0])), hitIds);
            }
            catch (Exception ex)
            {
                Assert.fail("Expected no exception, but got: " + ex.getMessage());
            }
     }
}
