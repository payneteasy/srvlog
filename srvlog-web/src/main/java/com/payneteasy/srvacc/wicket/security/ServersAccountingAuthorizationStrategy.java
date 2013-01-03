package com.payneteasy.srvacc.wicket.security;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.component.IRequestableComponent;
import org.springframework.security.access.annotation.Secured;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Date: 25.12.12 Time: 13:08
 */
public class ServersAccountingAuthorizationStrategy implements IAuthorizationStrategy{
    private static final String DEV_FILE_PATH = "src/main/resources/components-security.properties";
    private static final String PROD_FILE_PATH = "components-security.properties";

    /**
     * Used only in production mode (i.e. when properties are loaded from the classpath).
     */
    private Properties cachedProperties = null;

    @Override
    public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
        return true;
    }

    @Override
    public boolean isActionAuthorized(Component component, Action action) {
        boolean isAuthorized;
        String path = new StringBuilder()
                .append(component.getPage().getClass().getSimpleName())
                .append(".")
                .append(component.getId())
                .append(".")
                .append(action.getName()).toString();

        String superPath = new StringBuilder()
                .append(component.getPage().getClass().getSuperclass().getSimpleName())
                .append(".")
                .append(component.getId())
                .append(".")
                .append(action.getName()).toString();

        Properties securityProperties = loadSecurityProperties();
        if(isInSecurityPath(path, securityProperties)) {
            isAuthorized = isComponentAllowed(path, securityProperties);
        } else if(isInSecurityPath(superPath, securityProperties)) {
            isAuthorized = isComponentAllowed(superPath, securityProperties);
        } else if (component.getClass().isAnnotationPresent(Secured.class)) {
            isAuthorized = SecurityUtils.isComponentVisible(component.getClass());
        } else if (component instanceof BookmarkablePageLink<?>) {
            BookmarkablePageLink<?> link = (BookmarkablePageLink<?>) component;
            isAuthorized = SecurityUtils.isComponentVisible(link.getPageClass());
        } else {
            isAuthorized = true;
        }

        return isAuthorized;
    }

    private boolean isInSecurityPath(String path, Properties properties) {
        return properties.containsKey(path);
    }

    private boolean isComponentAllowed(String path, Properties properties){
        String line = properties.getProperty(path);
        StringTokenizer st = new StringTokenizer(line, "; , \t");
        while(st.hasMoreTokens()) {
            if(SecurityUtils.isUserInRole(st.nextToken())) {
                return true;
            }
        }
        return false;
    }

    private Properties loadSecurityProperties() {
        Properties prop = new Properties();
        String file = DEV_FILE_PATH;
        boolean devMode = new File(file).exists();
        InputStream in = null;
        try {
            if (devMode) {
                in = new FileInputStream(file);
            } else {
                if (cachedProperties != null) {
                    return cachedProperties;
                }
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl == null) {
                    cl = getClass().getClassLoader();
                }
                in = cl.getResourceAsStream(PROD_FILE_PATH);
            }
            prop.load(in);
            if (!devMode) {
                cachedProperties = prop;
            }
            return prop;
        } catch (IOException e) {
            throw new IllegalStateException("Error reading from file " +file+": "+e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException("Error closing file " +file+": "+e.getMessage(), e);
            }
        }
    }
}
