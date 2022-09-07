/*
 * Copyright 2022 CloudBees, Inc.
 * All rights reserved.
 */

package com.cloudbees.openfeature.provider;

import io.rollout.client.Core;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudbeesProviderTest {
    @Test
    public void testConstructorThrowsErrorIfAppKeyIsNotProvided() {
        assertThrows(NullPointerException.class, () -> new CloudbeesProvider(null));
        assertThrows(Core.SetupException.class, () -> new CloudbeesProvider(""));
    }
}
