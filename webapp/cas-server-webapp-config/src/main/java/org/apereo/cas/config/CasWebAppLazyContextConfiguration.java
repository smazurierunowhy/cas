package org.apereo.cas.config;

import org.apereo.cas.spring.ExcludeFromLazyInitialization;

import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.MethodMetadata;

/**
 * This is {@link CasWebAppLazyContextConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Profile("lazy")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration("casWebAppLazyContextConfiguration")
public class CasWebAppLazyContextConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BeanFactoryPostProcessor lazyInitBeanFactoryPostProcessor() {
        return new LazyInitBeanFactoryPostProcessor();
    }

    private static class LazyInitBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
            for (val beanName : beanFactory.getBeanDefinitionNames()) {
                val definition = beanFactory.getBeanDefinition(beanName);
                if (isLazy(definition, beanFactory)) {
                    definition.setLazyInit(true);
                }
            }
        }

        private static boolean isLazy(final BeanDefinition beanDefinition, final ConfigurableListableBeanFactory beanFactory) {
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                if (beanDefinition.getSource() instanceof MethodMetadata) {
                    val beanMethod = (MethodMetadata) beanDefinition.getSource();
                    return !beanMethod.isAnnotated(ExcludeFromLazyInitialization.class.getName());
                }
            }
            return true;
        }
    }
}
