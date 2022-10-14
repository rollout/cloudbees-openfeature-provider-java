/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.provider;

import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.Metadata;
import dev.openfeature.sdk.ProviderEvaluation;
import dev.openfeature.sdk.Reason;
import dev.openfeature.sdk.Value;
import dev.openfeature.sdk.exceptions.InvalidContextError;
import io.rollout.rox.server.Rox;
import io.rollout.rox.server.RoxOptions;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
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
        this(appKey, null);
    }

    /**
     * Creates a new CloudBees Provider.
     * @param appKey the CloudBees app key (also known as the environment ID)
     * @param options The {@link RoxOptions} to initialise with
     */
    public CloudbeesProvider(String appKey, @Nullable RoxOptions options) {
        try {
            Rox.setup(appKey, options).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Metadata getMetadata() {
        return () -> NAME;
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue, EvaluationContext ctx) {
        return ProviderEvaluation.<Boolean>builder()
                .value(Rox.dynamicAPI().isEnabled(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.TARGETING_MATCH.name())
                .build();
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue, EvaluationContext ctx) {
        return ProviderEvaluation.<String>builder()
                .value(Rox.dynamicAPI().getValue(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.TARGETING_MATCH.name())
                .build();
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue, EvaluationContext ctx) {
        return ProviderEvaluation.<Integer>builder()
                .value(Rox.dynamicAPI().getInt(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.TARGETING_MATCH.name())
                .build();
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String key, Double defaultValue, EvaluationContext ctx) {
        return ProviderEvaluation.<Double>builder()
                .value(Rox.dynamicAPI().getDouble(key, defaultValue, ContextTransformer.transform(ctx)))
                .variant(ROX)
                .reason(Reason.TARGETING_MATCH.name())
                .build();
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String s, Value value, EvaluationContext evaluationContext) {
        throw new InvalidContextError("Not implemented - CloudBees feature management does not support an 'Object' type. Only String, Number and Boolean");
    }
}
