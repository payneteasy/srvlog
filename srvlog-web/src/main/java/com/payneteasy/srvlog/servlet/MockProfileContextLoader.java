package com.payneteasy.srvlog.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

/**
 * ContextLoaderListener, который принудительно устанавливает mock-профиль до refresh.
 */
public class MockProfileContextLoader extends ContextLoaderListener {

    private static final Logger LOG = LoggerFactory.getLogger(MockProfileContextLoader.class);

    @Override
    protected void customizeContext(jakarta.servlet.ServletContext servletContext, ConfigurableWebApplicationContext context) {
        super.customizeContext(servletContext, context);
        String profile = servletContext.getInitParameter("spring.profiles.active");
        if (profile == null || profile.isEmpty()) {
            String mockData = System.getenv("MOCK_DATA");
            if ("1".equals(mockData) || "true".equalsIgnoreCase(mockData)) {
                profile = "mock";
            }
        }
        if (profile != null && !profile.isEmpty()) {
            context.getEnvironment().setActiveProfiles(profile);
            LOG.info("MockProfileContextLoader: setActiveProfiles([{}]) AFTER super", profile);
        }
    }
}
