/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.test;

import com.boxalino.examples.DataFullExport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 *
 * @author HASHIR
 */
public class DataFullExportTest {

    private DataFullExport servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    public DataFullExportTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        servlet = new DataFullExport();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    @Test
    public void testBackendDataFullExport() {

        try {
            servlet.account = this.account;
            servlet.password = this.password;
            servlet.print = false;
            servlet.doGet(request, response);
        } catch (ServletException ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        } catch (IOException ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }

    }
}
