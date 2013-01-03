package com.payneteasy.srvacc.wicket.page;

import com.payneteasy.srvacc.wicket.security.SecurityUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.springframework.security.access.annotation.Secured;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Date: 21.12.12 Time: 15:53
 */
@Secured("ROLE_ADMIN")
public class BasePage extends WebPage{
    public BasePage() {
        add(new Label("label", "Hello "+ SecurityUtils.getUsername()+ "! You have roles "+ Arrays.toString(SecurityUtils.getRoles())));

        add(new Label("admin-label", "Admin label"));
    }
}
