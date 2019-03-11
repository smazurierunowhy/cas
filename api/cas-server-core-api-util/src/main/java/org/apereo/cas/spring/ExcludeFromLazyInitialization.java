package org.apereo.cas.spring;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This is {@link ExcludeFromLazyInitialization}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ExcludeFromLazyInitialization {
}
