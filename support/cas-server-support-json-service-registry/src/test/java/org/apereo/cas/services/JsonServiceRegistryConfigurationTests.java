package org.apereo.cas.services;

import org.apereo.cas.category.FileSystemCategory;
import org.apereo.cas.config.CasCoreServicesConfiguration;
import org.apereo.cas.config.CasCoreUtilConfiguration;
import org.apereo.cas.config.JsonServiceRegistryConfiguration;
import org.apereo.cas.config.support.CasWebApplicationServiceFactoryConfiguration;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.util.junit.ConditionalIgnoreRule;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.io.File;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * This is {@link JsonServiceRegistryConfigurationTests}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    RefreshAutoConfiguration.class,
    JsonServiceRegistryConfiguration.class,
    CasCoreServicesConfiguration.class,
    CasWebApplicationServiceFactoryConfiguration.class,
    CasCoreUtilConfiguration.class
})
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
@Category(FileSystemCategory.class)
public class JsonServiceRegistryConfigurationTests {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    static {
        try {
            val dir = new File(FileUtils.getTempDirectory(), "cas-json-services");
            if (dir.exists()) {
                FileUtils.deleteDirectory(dir);
            }
            FileUtils.forceMkdir(dir);
            assertTrue(dir.exists());
            System.setProperty("cas.serviceRegistry.json.location", "file://" + dir.getCanonicalPath());
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Test
    public void verifyOperation() {
        val svc1 = RegisteredServiceTestUtils.getRegisteredService("sample-service");
        svc1.setExpirationPolicy(new DefaultRegisteredServiceExpirationPolicy(true, LocalDate.now()));
        servicesManager.save(svc1);

        val svc2 = (AbstractRegisteredService) servicesManager.findServiceBy(svc1.getId());
        val accessStrategy = new DefaultRegisteredServiceAccessStrategy(false, true);
        accessStrategy.setDelegatedAuthenticationPolicy(
            new DefaultRegisteredServiceDelegatedAuthenticationPolicy(CollectionUtils.wrapList("something")));
        svc2.setAccessStrategy(accessStrategy);
        servicesManager.save(svc2);
    }
}
