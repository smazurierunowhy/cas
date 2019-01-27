package org.apereo.cas.services.util;

import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceChangelogManager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * This is {@link RegisteredServiceSimpleBeanPropertyFilter}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@RequiredArgsConstructor
@Slf4j
public class RegisteredServiceSimpleBeanPropertyFilter extends SimpleBeanPropertyFilter implements RegisteredServiceChangelogManager {

    /**
     * Name of this filter.
     */
    public static final String FILTER_NAME = "RegisteredServiceResourceDeltaSimpleBeanPropertyFilter";

    private final RegisteredServiceChangelogManager changelogManager;

    @Override
    public void serializeAsField(final Object pojo, final JsonGenerator jgen, final SerializerProvider provider, final PropertyWriter writer) throws Exception {
        if (canWriteProperty((RegisteredService) pojo, writer)) {
            LOGGER.trace("Serializing field [{}]", writer.getName());
            super.serializeAsField(pojo, jgen, provider, writer);
        } else {
            LOGGER.trace("Ignored unchanged field [{}] for serialization", writer.getName());
        }
    }

    private boolean canWriteProperty(final RegisteredService givenService, final PropertyWriter writer) {
        val latest = changelogManager.getLatestVersion(givenService);
        if (latest == null) {
            return true;
        }
        val changes = changelogManager.compare(latest, givenService);
        return false;
    }
}
