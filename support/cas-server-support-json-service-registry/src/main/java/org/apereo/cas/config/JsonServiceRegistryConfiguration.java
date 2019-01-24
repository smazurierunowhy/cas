package org.apereo.cas.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.JsonServiceRegistry;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ServiceRegistry;
import org.apereo.cas.services.ServiceRegistryExecutionPlan;
import org.apereo.cas.services.ServiceRegistryExecutionPlanConfigurer;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.services.replication.RegisteredServiceReplicationStrategy;
import org.apereo.cas.services.resource.RegisteredServiceResourceNamingStrategy;
import org.apereo.cas.services.util.CasAddonsRegisteredServicesJsonSerializer;
import org.apereo.cas.services.util.RegisteredServiceJsonSerializer;
import org.apereo.cas.services.util.RegisteredServiceSimpleBeanPropertyFilter;
import org.apereo.cas.util.CollectionUtils;
import org.apereo.cas.util.serialization.StringSerializer;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Collection;

/**
 * This is {@link JsonServiceRegistryConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Configuration("jsonServiceRegistryConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 1)
@ConditionalOnProperty(prefix = "cas.serviceRegistry.json", name = "location")
public class JsonServiceRegistryConfiguration {

    @Autowired
    @Qualifier("servicesManager")
    private ObjectProvider<ServicesManager> servicesManager;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("registeredServiceReplicationStrategy")
    private ObjectProvider<RegisteredServiceReplicationStrategy> registeredServiceReplicationStrategy;

    @Autowired
    @Qualifier("registeredServiceResourceNamingStrategy")
    private ObjectProvider<RegisteredServiceResourceNamingStrategy> resourceNamingStrategy;

    @Bean
    @SneakyThrows
    public ServiceRegistry jsonServiceRegistry() {
        val registry = casProperties.getServiceRegistry();
        return new JsonServiceRegistry(registry.getJson().getLocation(),
            registry.isWatcherEnabled(),
            eventPublisher,
            registeredServiceReplicationStrategy.getIfAvailable(),
            resourceNamingStrategy.getIfAvailable(),
            registeredServiceJsonSerializers());
    }

    @ConditionalOnMissingBean(name = "registeredServiceJsonSerializers")
    @Bean
    public Collection<StringSerializer<RegisteredService>> registeredServiceJsonSerializers() {
        return CollectionUtils.wrapList(new CasAddonsRegisteredServicesJsonSerializer(), registeredServiceJsonSerializer());
    }

    @ConditionalOnMissingBean(name = "registeredServiceJsonSerializer")
    @Bean
    public StringSerializer<RegisteredService> registeredServiceJsonSerializer() {
        val filter = new SimpleFilterProvider();
        filter.addFilter(RegisteredServiceSimpleBeanPropertyFilter.FILTER_NAME,
            new RegisteredServiceSimpleBeanPropertyFilter(servicesManager.getIfAvailable()));
        return new RegisteredServiceJsonSerializer(filter);
    }

    @Bean
    @ConditionalOnMissingBean(name = "jsonServiceRegistryExecutionPlanConfigurer")
    public ServiceRegistryExecutionPlanConfigurer jsonServiceRegistryExecutionPlanConfigurer() {
        return new ServiceRegistryExecutionPlanConfigurer() {
            @Override
            public void configureServiceRegistry(final ServiceRegistryExecutionPlan plan) {
                plan.registerServiceRegistry(jsonServiceRegistry());
            }
        };
    }
}
