package org.apereo.cas.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This is {@link JsonServiceRegistryTestsSuite}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    JsonServiceRegistryTests.class,
    JsonServiceRegistryConfigurationTests.class
})
public class JsonServiceRegistryTestsSuite {
}
