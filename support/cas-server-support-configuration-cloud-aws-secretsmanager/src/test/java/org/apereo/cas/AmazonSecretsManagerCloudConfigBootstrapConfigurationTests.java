package org.apereo.cas;

import org.apereo.cas.category.AmazonWebServicesSecretsManagerCategory;
import org.apereo.cas.config.AmazonSecretsManagerCloudConfigBootstrapConfiguration;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.util.junit.ConditionalIgnoreRule;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

/**
 * This is {@link AmazonSecretsManagerCloudConfigBootstrapConfigurationTests}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    AmazonSecretsManagerCloudConfigBootstrapConfiguration.class
})
//@ConditionalIgnore(condition = RunningContinuousIntegrationCondition.class, port = 4584)
@Category(AmazonWebServicesSecretsManagerCategory.class)
@TestPropertySource(properties = {
    "cas.spring.cloud.aws.secretsManager.endpoint=" + AmazonSecretsManagerCloudConfigBootstrapConfigurationTests.ENDPOINT,
    "cas.spring.cloud.aws.secretsManager.credentialAccessKey=" + AmazonSecretsManagerCloudConfigBootstrapConfigurationTests.CREDENTIAL_ACCESS_KEY,
    "cas.spring.cloud.aws.secretsManager.credentialSecretKey=" + AmazonSecretsManagerCloudConfigBootstrapConfigurationTests.CREDENTIAL_SECRET_KEY
})
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class AmazonSecretsManagerCloudConfigBootstrapConfigurationTests {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    static final String ENDPOINT = "http://127.0.0.1:4584";
    static final String CREDENTIAL_SECRET_KEY = "test";
    static final String CREDENTIAL_ACCESS_KEY = "test";

    private static final String STATIC_AUTHN_USERS = "casuser::WHATEVER";

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

    @Autowired
    private CasConfigurationProperties casProperties;

    @BeforeClass
    public static void initialize() {
//        val environment = new MockEnvironment();
//        environment.setProperty(AmazonSecretsManagerCloudConfigBootstrapConfiguration.CAS_CONFIGURATION_PREFIX + '.' + "endpoint", ENDPOINT);
//        environment.setProperty(AmazonSecretsManagerCloudConfigBootstrapConfiguration.CAS_CONFIGURATION_PREFIX + '.' + "credentialAccessKey", CREDENTIAL_ACCESS_KEY);
//        environment.setProperty(AmazonSecretsManagerCloudConfigBootstrapConfiguration.CAS_CONFIGURATION_PREFIX + '.' + "credentialSecretKey", CREDENTIAL_SECRET_KEY);
//
//        val builder = new AmazonEnvironmentAwareClientBuilder(AmazonSecretsManagerCloudConfigBootstrapConfiguration.CAS_CONFIGURATION_PREFIX, environment);
//        val client = builder.build(AWSSecretsManagerClientBuilder.standard(), AWSSecretsManager.class);
//
//        val request = new PutSecretValueRequest();
//        request.setSecretId("cas.authn.accept.users");
//        request.setSecretString(STATIC_AUTHN_USERS);
//        client.putSecretValue(request);
    }

    @Test
    public void verifyOperation() {


    }
}
