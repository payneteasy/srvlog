package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Date: 22.12.12 Time: 13:43
 */
public class LoginPage extends WebPage {

    public LoginPage() {
        final String relativeUrl = getRequest().getContextPath();

        Form<Void> loginForm = new Form<Void>("loginForm");
        loginForm.add(new AttributeModifier("action", Model.<String>of(buildCheckUrl(relativeUrl))));
        add(loginForm);

        //login field
        RequiredTextField<String> loginInputField = new RequiredTextField<String>(J_USERNAME, Model.<String>of(""));
        loginForm.add(loginInputField);

        //password filed
        PasswordTextField passwordInputField = new PasswordTextField(J_PASSWORD, Model.<String>of(""));
        loginForm.add(passwordInputField);

        //button
        loginForm.add(new Button("loginButton"));

        addMessage(loginForm);

    }

    public static String buildCheckUrl(String relativePath) {
        StringBuilder sb = new StringBuilder();
        String postfix = J_SPRING_SECURITY_CHECK;
        if ("".equals(relativePath)) {
            sb.append("/").append(postfix);
        }  else{
            sb.append(relativePath).append("/").append(postfix);
        }
        return sb.toString();
    }

    protected String getSpringSecurityExceptionMessageAndRemoveException() {
        ServletWebRequest servletWebRequest = (ServletWebRequest) getRequest();
        HttpServletRequest request = servletWebRequest.getContainerRequest();
        HttpSession session = request.getSession(false);
        Object ex = null;
        if (session != null) {
            ex = session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            if (ex != null) {
                session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            }
        }
        String result = null;
        if (ex != null) {
            result = "The username or password you entered is incorrect or user is locked.";
        }
        return result;
    }

    protected void addMessage(Form<Void> form) {
        String message = getSpringSecurityExceptionMessageAndRemoveException();
        Label reasonLabel = new Label("reason", message);
        reasonLabel.setVisible(message != null);
        form.add(reasonLabel);
        add(form);
    }

    public static final String J_USERNAME = "j_username";
    public static final String J_PASSWORD = "j_password";
    public static final String J_SPRING_SECURITY_CHECK = "j_spring_security_check";
}
