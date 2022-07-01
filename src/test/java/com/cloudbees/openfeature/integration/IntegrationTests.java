/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.integration;

import com.cloudbees.openfeature.provider.CloudbeesProvider;
import dev.openfeature.javasdk.Client;
import dev.openfeature.javasdk.EvaluationContext;
import dev.openfeature.javasdk.OpenFeatureAPI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IntegrationTests {
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
        assertThat(client.getBooleanValue("boolean-with-context", false, new EvaluationContext().withStringAttribute("stringproperty", "on")), is(true));
        assertThat(client.getBooleanValue("boolean-with-context", false, new EvaluationContext().withStringAttribute("stringproperty", "off")), is(false));
    }

    @Test
    public void testStringFlags() {
        // Targeting on.
        assertThat(client.getStringValue("string-static-yes", "default"), is("yes"));
        assertThat(client.getStringValue("string-static-no", "default"), is("no"));

        // Targeting off - uses the default values
        assertThat(client.getStringValue("string-disabled", "banana"), is("banana"));

        // Using a context
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().withStringAttribute("stringproperty", "on")), is("yes"));
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().withStringAttribute("stringproperty", "off")), is("no"));
        assertThat(client.getStringValue("string-with-context", "default", new EvaluationContext().withStringAttribute("not defined", "whatever")), is("not specified"));
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
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().withStringAttribute("stringproperty", "1")), is(1));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().withStringAttribute("stringproperty", "5")), is(5));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext().withStringAttribute("not defined", "whatever")), is(10));
        assertThat(client.getIntegerValue("integer-with-context", -1, new EvaluationContext()), is(10));
        assertThat(client.getIntegerValue("integer-with-context", -1), is(10));
    }
}
