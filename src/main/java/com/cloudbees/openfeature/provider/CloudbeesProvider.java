/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.provider;

import dev.openfeature.javasdk.EvaluationContext;
import dev.openfeature.javasdk.FeatureProvider;
import dev.openfeature.javasdk.FlagEvaluationOptions;
import dev.openfeature.javasdk.Metadata;
import dev.openfeature.javasdk.ProviderEvaluation;
import dev.openfeature.javasdk.Reason;
import io.rollout.rox.server.Rox;
import java.util.concurrent.ExecutionException;
import lombok.Getter;

public class CloudbeesProvider implements FeatureProvider {

    private static final String ROX = "Rox";

    @Getter
    private static final String NAME = "CloudBees Provider";

    /**
     * Creates a new CloudBees Provider.
     * @param appKey the CloudBees app key (also known as the environment ID)
     */
    public CloudbeesProvider(String appKey) {
        try {
            Rox.setup(appKey).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Metadata getMetadata() {
        return () -> NAME;
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue, EvaluationContext ctx, FlagEvaluationOptions options) {
        return ProviderEvaluation.<Boolean>builder()
                .value(Rox.dynamicAPI().isEnabled(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.DEFAULT)
                .build();
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue, EvaluationContext ctx, FlagEvaluationOptions options) {
        return ProviderEvaluation.<String>builder()
                .value(Rox.dynamicAPI().getValue(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.DEFAULT)
                .build();
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue, EvaluationContext ctx, FlagEvaluationOptions options) {
        return ProviderEvaluation.<Integer>builder()
                .value(Rox.dynamicAPI().getInt(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.DEFAULT)
                .build();
    }

    @Override
    public <T> ProviderEvaluation<T> getObjectEvaluation(String key, T defaultValue, EvaluationContext invocationContext, FlagEvaluationOptions options) {
        throw new RuntimeException("Not implemented - CloudBees feature management does not support an 'Object' type. Only String, Number and Boolean");
    }
}
