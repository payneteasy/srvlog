package com.payneteasy.srvlog.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * Явно устанавливает mock-профиль до создания бинов Spring.
 * Решает проблему, когда context-param не успевает примениться к component-scan.
 */
public class MockProfileContextInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

    private static final Logger LOG = LoggerFactory.getLogger(MockProfileContextInitializer.class);

    @Override
    public void initialize(ConfigurableWebApplicationContext applicationContext) {
        String profile = null;
        if (applicationContext.getServletContext() != null) {
            profile = applicationContext.getServletContext().getInitParameter("spring.profiles.active");
        }
        if (profile == null || profile.isEmpty()) {
            String mockData = System.getenv("MOCK_DATA");
            if ("1".equals(mockData) || "true".equalsIgnoreCase(mockData)) {
                profile = "mock";
            }
        }
        if (profile != null && !profile.isEmpty()) {
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            env.setActiveProfiles(profile);
            LOG.info("ApplicationContextInitializer: set active profiles to [{}]", profile);
        }
    }
}
