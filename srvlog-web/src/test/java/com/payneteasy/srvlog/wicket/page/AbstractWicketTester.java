package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.wicket.SrvlogUIApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Date: 09.01.13
 */
public abstract class AbstractWicketTester {
    private  WicketTester wicketTester = null;
    private  ApplicationContextMock applicationContextMock = null;

    @Before
    public void setup(){
        applicationContextMock = new ApplicationContextMock();
        addBean(applicationContextMock);

        wicketTester = new WicketTester(new SrvlogUIApplication(){
            @Override
            protected void addSpringComponentInjector() {
                getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContextMock, true));
            }
        });

        addSecurityContextHolderMock();
        setupTest();
    }

    protected void addBean(ApplicationContextMock applicationContextMock) { /* override if you want add beans*/ }

    /**
     * Adds bean to the mock application context
     */
    protected void addBean(String beanName, Object mock){
       applicationContextMock.putBean(beanName, mock);
    }

    @After
    public void destroy(){
       clearSecurityContextHolderMock();
       wicketTester.destroy();
    }

    protected abstract void setupTest();

    public  WicketTester getWicketTester() {
        return wicketTester;
    }

    public ApplicationContextMock getApplicationContextMock() {
        return applicationContextMock;
    }

    protected void addSecurityContextHolderMock(){
        User user = new User("admin", "admin", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected void clearSecurityContextHolderMock(){
        SecurityContextHolder.clearContext();
    }

    protected Map<LogLevel, Long> getDefaultLogsBySeverityMap(){
        Map<LogLevel, Long> map = new TreeMap<>();
        for (LogLevel logLevel : LogLevel.values()) {
            map.put(logLevel, 1L);
        }
        return map;
    }

}
