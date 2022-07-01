/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.provider;

import dev.openfeature.javasdk.EvaluationContext;
import io.rollout.context.Context;

/**
 * A class that transforms the context from an OpenFeature {@link EvaluationContext} into the Rox {@link Context}.
 */
class ContextTransformer {
    static Context transform(EvaluationContext ctx) {
        return new Context.Builder().from(ctx.toMap());
    }
}
