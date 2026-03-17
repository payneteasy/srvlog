package com.payneteasy.srvlog.service.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition: true when mock mode is enabled (srvlog.mock=true or spring.profiles.active=mock).
 * Used because @Profile may not apply correctly in XML-configured contexts.
 */
public class MockModeCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String mockEnv = System.getenv("MOCK_DATA");
        if ("1".equals(mockEnv) || "true".equalsIgnoreCase(mockEnv)) {
            return true;
        }
        String mockProp = context.getEnvironment().getProperty("srvlog.mock.enabled");
        if ("true".equalsIgnoreCase(mockProp)) {
            return true;
        }
        String profile = context.getEnvironment().getProperty("spring.profiles.active");
        return "mock".equals(profile);
    }
}
