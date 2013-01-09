package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 22.12.12 Time: 13:38
 */
public class LoginPageTest{

    private static WicketTester wicketTester = null;

    @BeforeClass
    public static void testSetup() {
        wicketTester = new WicketTester();
        wicketTester.startPage(LoginPage.class);
    }

    @Test
    public void testLoginPage(){
        wicketTester.assertRenderedPage(LoginPage.class);

        wicketTester.assertComponent("loginForm", Form.class);
//        Form form = (Form) wicketTester.getComponentFromLastRenderedPage("loginForm");
//        assertEquals("", LoginPage.buildCheckUrl(""), form.getMarkupAttributes().getStringValue("action"));

        wicketTester.assertComponent("loginForm:loginLabel", Label.class);
        wicketTester.assertComponent("loginForm:j_username", RequiredTextField.class);
        TextField username = (TextField) wicketTester.getComponentFromLastRenderedPage("loginForm:j_username");
        assertEquals("Username input field name property should be equal to j_username in order to support spring-security", "j_username", username.getInputName());
        wicketTester.assertComponent("loginForm:passwordLabel", Label.class);
        wicketTester.assertComponent("loginForm:j_password", PasswordTextField.class);

        TextField password = (TextField) wicketTester.getComponentFromLastRenderedPage("loginForm:j_password");
        assertEquals("Password input field name property should be equal to j_password in order to support spring-security", "j_password", password.getInputName());

        wicketTester.assertComponent("loginForm:loginButton", Button.class);

    }

}
