package org.apereo.cas.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is {@link RegisteredServiceChangelogManager}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
public interface RegisteredServiceChangelogManager extends Serializable {

    /**
     * Commit the service object into the changelog.
     *
     * @param service the service
     */
    default void commit(final RegisteredService service) {
    }

    /**
     * Delete.
     *
     * @param service the service
     */
    default void delete(final RegisteredService service) {
    }

    /**
     * Compare objects to find delta.
     *
     * @param oldService the old service
     * @param newService the new service
     * @return the list
     */
    default List compare(final RegisteredService oldService, final RegisteredService newService) {
        return new ArrayList();
    }

    default RegisteredService getLatestVersion(final RegisteredService service) {
        return null;
    }
}
