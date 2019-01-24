package org.apereo.cas.services.util;

import org.apereo.cas.services.AbstractRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.services.resource.RegisteredServiceResourceDeltaExtractor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;

/**
 * This is {@link RegisteredServiceSimpleBeanPropertyFilter}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@RequiredArgsConstructor
@Slf4j
public class RegisteredServiceSimpleBeanPropertyFilter extends SimpleBeanPropertyFilter implements RegisteredServiceResourceDeltaExtractor {

    /**
     * Name of this filter.
     */
    public static final String FILTER_NAME = "RegisteredServiceResourceDeltaSimpleBeanPropertyFilter";

    private static final Javers JAVERS = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();

    private final ServicesManager servicesManager;

    @Override
    public void serializeAsField(final Object pojo, final JsonGenerator jgen, final SerializerProvider provider, final PropertyWriter writer) throws Exception {
        if (canWriteProperty(provider, (RegisteredService) pojo, writer)) {
            LOGGER.trace("Serializing field [{}]", writer.getName());
            super.serializeAsField(pojo, jgen, provider, writer);
        } else {
            LOGGER.trace("Ignored unchanged field [{}] for serialization", writer.getName());
        }
    }

    private boolean canWriteProperty(final SerializerProvider provider, final RegisteredService givenService, final PropertyWriter writer) {
        var service = servicesManager.findServiceBy(givenService.getId());
        if (service == null) {
            service = ((AbstractRegisteredService) givenService).newInstance();
            service.setId(givenService.getId());
        }
        val diff = JAVERS.compare(service, givenService);
        LOGGER.trace("Examining differences for registered service [{}] and field [{}]", givenService.getName(), writer.getName());
        val changes = diff.getPropertyChanges(writer.getName());
        return !changes.isEmpty();
    }
}
