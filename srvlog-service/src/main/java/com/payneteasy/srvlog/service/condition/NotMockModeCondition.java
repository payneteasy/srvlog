package com.payneteasy.srvlog.service.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition: true when mock mode is NOT enabled.
 */
public class NotMockModeCondition implements Condition {

    private static final MockModeCondition MOCK_CONDITION = new MockModeCondition();

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return !MOCK_CONDITION.matches(context, metadata);
    }
}
