package com.payneteasy.srvlog.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.io.IOException;

/**
 * Date: 28.01.13 Time: 12:10
 */
public class CustomDelegationFilterProxy extends DelegatingFilterProxy {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String skipSpringSecurity = getServletContext().getInitParameter("skipSpringSecurity");
        if(skipSpringSecurity!=null && isTrue(skipSpringSecurity)){
            // Ignore the DelegatingProxyFilter delegate
            filterChain.doFilter(request, response);
        }else {
            // Call the delegate
            super.doFilter(request, response, filterChain);
        }
    }

    private boolean isTrue(String type){
        return "true".equals(type);
    }
}
