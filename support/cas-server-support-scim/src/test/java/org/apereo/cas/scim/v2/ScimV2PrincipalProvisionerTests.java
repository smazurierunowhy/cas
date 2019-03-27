package org.apereo.cas.scim.v2;

import org.apereo.cas.api.PrincipalProvisioner;
import org.apereo.cas.config.CasCoreAuthenticationConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationServiceSelectionStrategyConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationSupportConfiguration;
import org.apereo.cas.config.CasCoreConfiguration;
import org.apereo.cas.config.CasCoreHttpConfiguration;
import org.apereo.cas.config.CasCoreServicesConfiguration;
import org.apereo.cas.config.CasCoreUtilConfiguration;
import org.apereo.cas.config.CasCoreWebConfiguration;
import org.apereo.cas.config.CasRegisteredServicesTestConfiguration;
import org.apereo.cas.config.CasScimConfiguration;
import org.apereo.cas.config.support.CasWebApplicationServiceFactoryConfiguration;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.RegisteredServiceTestUtils;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.web.flow.config.CasCoreWebflowConfiguration;
import org.apereo.cas.web.flow.config.CasWebflowContextConfiguration;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link ScimV2PrincipalProvisionerTests}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@SpringBootTest(classes = {
    CasScimConfiguration.class,
    RefreshAutoConfiguration.class,
    CasCoreWebflowConfiguration.class,
    CasWebflowContextConfiguration.class,
    CasCoreServicesConfiguration.class,
    CasCoreWebConfiguration.class,
    CasCoreHttpConfiguration.class,
    CasCoreConfiguration.class,
    CasCoreAuthenticationConfiguration.class,
    CasRegisteredServicesTestConfiguration.class,
    CasWebApplicationServiceFactoryConfiguration.class,
    CasCoreAuthenticationSupportConfiguration.class,
    CasCoreAuthenticationServiceSelectionStrategyConfiguration.class,
    CasCoreUtilConfiguration.class
})
@EnableConfigurationProperties(CasConfigurationProperties.class)
@TestPropertySource(properties = {
    "cas.scim.version=2",
    "cas.scim.target=http://localhost:8080/v2"
})
public class ScimV2PrincipalProvisionerTests {
    @Autowired
    @Qualifier("scimProvisioner")
    private PrincipalProvisioner scimProvisioner;

    @Test
    public void verifyScimOps() {
        assertNotNull(scimProvisioner);
        val principal = RegisteredServiceTestUtils.getPrincipal("casuser", CollectionUtils.wrap("firstName", "CAS"));
        val credentials = RegisteredServiceTestUtils.getCredentialsWithSameUsernameAndPassword("testcredentials");
        val authn = RegisteredServiceTestUtils.getAuthentication(principal);
        val result = scimProvisioner.create(authn, principal, credentials);
        assertTrue(result);
    }
}
