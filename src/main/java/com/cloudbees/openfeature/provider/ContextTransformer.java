/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.provider;

import dev.openfeature.javasdk.EvaluationContext;
import dev.openfeature.javasdk.Value;
import io.rollout.context.Context;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class that transforms the context from an OpenFeature {@link EvaluationContext} into the Rox {@link Context}.
 */
class ContextTransformer {
    static Context transform(EvaluationContext ctx) {
        Map<String, Object> contextAsMap = ctx.asMap()
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                e -> getValueAsObject(e.getValue())
                        ));
        return new Context.Builder().from(contextAsMap);
    }

    private static Object getValueAsObject(Value value) {
        // io.rollout.context takes in a Map<String, Object>, where the value is a Custom Property
        // Try to build the map as correctly as possible
        if (value.isString()) {
            return value.asString();
        } else if (value.isInteger()) {
            return value.asInteger();
        } else if (value.isDouble()) {
            return value.asDouble();
        } else if (value.isBoolean()) {
            return value.asBoolean();
        }

        // TODO - What do we do with the rox SemVer type?

        // and throw an exception for non-supperted types.
        throw new IllegalArgumentException("CloudBees Provider SDK only supports strings as Evaluation Context values");
    }
}
