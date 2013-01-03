package com.payneteasy.srvacc.wicket.security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Date: 25.12.12 Time: 13:11
 */
public class SecurityUtils {

    //TODO cover by unit test
    public static boolean isUserInRole(String aRole){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authentication.getPrincipal() == null || authorities == null) {
            return false;
        }

        for (GrantedAuthority authority : authorities) {
            if (aRole.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    //TODO cover by unit test
    public static String[] getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return null;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authentication.getPrincipal() == null || authorities == null) {
            return null;
        }
        String[] roles = new String[authorities.size()];
        int i = 0;
        for (GrantedAuthority authority : authorities) {
            roles[i] = authority.getAuthority();
            i++;
        }
        return roles;
    }

    //TODO cover by unit test
    public static String getUsername() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        return username;
    }

    //TODO cover by unit test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean isComponentVisible(Class aComponentClass) {
        if (aComponentClass.isAnnotationPresent(Secured.class)) {
            Secured securedAnnotation = (Secured) aComponentClass.getAnnotation(Secured.class);
            String[] roles = securedAnnotation.value();
            if (roles != null && roles.length > 0) {
                for (String role : roles) {
                    if (isUserInRole(role)) {
                        return true;
                    }
                }
                return false;
            } else {
                throw new RuntimeException("Page " + aComponentClass.getName() + " has no roles in Secured annotation");
            }

        } else {
            throw new RuntimeException("Page " + aComponentClass.getName() + " has no Secured annotation");
        }
    }

}
