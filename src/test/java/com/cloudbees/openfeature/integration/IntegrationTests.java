/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.integration;

import com.cloudbees.openfeature.provider.CloudbeesProvider;
import dev.openfeature.javasdk.Client;
import dev.openfeature.javasdk.EvaluationContext;
import dev.openfeature.javasdk.FlagEvaluationDetails;
import dev.openfeature.javasdk.OpenFeatureAPI;
import dev.openfeature.javasdk.Reason;
import dev.openfeature.javasdk.Structure;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IntegrationTests {
    public static final String STRING_PROPERTY = "stringproperty";
    public static final String NUMBER_PROPERTY = "numberproperty";
    public static final String BOOLEAN_PROPERTY = "booleanproperty";
    static Client client;

    @BeforeAll
    public static void setup() {
        OpenFeatureAPI api = OpenFeatureAPI.getInstance();
        api.setProvider(new CloudbeesProvider("62bee5bbca1059d18808adad"));
        client = api.getClient();
    }

    @Test
    public void testBooleanFlags() {
        // Targeting on.
        assertThat(client.getBooleanValue("boolean-static-true", false), is(true));
        assertThat(client.getBooleanValue("boolean-static-false", true), is(false));

        // Targeting off - uses the default values
        assertThat(client.getBooleanValue("boolean-disabled", false), is(false));
        assertThat(client.getBooleanValue("boolean-disabled", true), is(true));

        // Using a context
        assertThat(client.getBooleanValue("boolean-with-context", false, new EvaluationContext().add(STRING_PROPERTY, "on")), is(true));
        assertThat(client.getBooleanValue("boolean-with-context", false, new EvaluationContext().add(STRING_PROPERTY, "off")), is(false));
    }

    @Test
    public void testStringFlags() {
        // Targeting on.
        assertThat(client.getStringValue("string-static-yes", "default"), is("yes"));
        assertThat(client.getStringValue("string-static-no", "default"), is("no"));

        // Targeting off - uses the default values
        assertThat(client.getStringValue("string-disabled", "banana"), is("banana"));

        // Using a context
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().add(STRING_PROPERTY, "on")), is("yes"));
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().add(STRING_PROPERTY, "off")), is("no"));
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().add("not defined", "whatever")), is("not specified"));
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext()), is("not specified"));
        assertThat(client.getStringValue("string-with-context", "default"), is("not specified"));
    }

    @Test
    public void testIntegerFlags() {
        // Targeting on.
        assertThat(client.getIntegerValue("integer-static-5", 5), is(5));

        // Targeting off - uses the default values
        assertThat(client.getIntegerValue("integer-disabled", 7), is(7));

        // Using a context
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().add(STRING_PROPERTY, "1")), is(1));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().add(STRING_PROPERTY, "5")), is(5));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().add("not defined", "whatever")), is(10));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext()), is(10));
        assertThat(client.getIntegerValue("integer-with-context", -1), is(10));
    }

    @Test
    public void testFlagsWithDifferentlyTypedContextObjects() {
        // Test positive matches for supported types (string/number/boolean)
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(STRING_PROPERTY, "one")), is(1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(NUMBER_PROPERTY, 1)), is(1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(NUMBER_PROPERTY, 1.0)), is(1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(BOOLEAN_PROPERTY, true)), is(1));

        // Test negative matches for supported types (string/number/boolean) - it should serve the default value
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(STRING_PROPERTY, "no")), is(-1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(NUMBER_PROPERTY, 0)), is(-1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(NUMBER_PROPERTY, 0.0)), is(-1));
        assertThat(client.getIntegerValue("integer-with-complex-context", -1, new EvaluationContext().add(BOOLEAN_PROPERTY, false)), is(-1));

        // Test that other types of context values throw an error
        FlagEvaluationDetails<Integer> evaluationDetails = client.getIntegerDetails("integer-with-complex-context", -1, new EvaluationContext().add("badproperty", Collections.emptyList()));
        assertThat(evaluationDetails.getValue(), is(-1)); // default
        assertThat(evaluationDetails.getReason(), is(Reason.ERROR));
        assertThat(evaluationDetails.getErrorCode(), is("CloudBees Provider SDK only supports strings/numbers/booleans as Evaluation Context values"));

        evaluationDetails = client.getIntegerDetails("integer-with-complex-context", -1, new EvaluationContext().add("badproperty", new Structure()));
        assertThat(evaluationDetails.getValue(), is(-1)); // default
        assertThat(evaluationDetails.getReason(), is(Reason.ERROR));
        assertThat(evaluationDetails.getErrorCode(), is("CloudBees Provider SDK only supports strings/numbers/booleans as Evaluation Context values"));

        evaluationDetails = client.getIntegerDetails("integer-with-complex-context", -1, new EvaluationContext().add("badproperty", Instant.now().atZone(ZoneId.systemDefault())));
        assertThat(evaluationDetails.getValue(), is(-1)); // default
        assertThat(evaluationDetails.getReason(), is(Reason.ERROR));
        assertThat(evaluationDetails.getErrorCode(), is("CloudBees Provider SDK only supports strings/numbers/booleans as Evaluation Context values"));
    }
}
