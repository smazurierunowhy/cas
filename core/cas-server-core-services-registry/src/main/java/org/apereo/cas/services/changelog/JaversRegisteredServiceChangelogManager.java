package org.apereo.cas.services.changelog;

import org.apereo.cas.services.AbstractRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceChangelogManager;

import lombok.val;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This is {@link JaversRegisteredServiceChangelogManager}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
public class JaversRegisteredServiceChangelogManager implements RegisteredServiceChangelogManager {

    private final Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();

    @Override
    public void commit(final RegisteredService service) {
        javers.commit(service.getName(), service);
    }

    @Override
    public void delete(final RegisteredService service) {
        javers.commitShallowDelete(service.getName(), service);
    }

    @Override
    public List compare(final RegisteredService oldService, final RegisteredService newService) {
        val shadows = javers.compare(oldService, newService);
       return new ArrayList();
    }

    @Override
    public RegisteredService getLatestVersion(final RegisteredService service) {
        val query = QueryBuilder.byInstanceId(service.getId(), service.getClass())
            .withChildValueObjects()
            .withNewObjectChanges()
            .withScopeDeepPlus(1)
            .build();
        val shadows = javers.findShadows(query);
        if (shadows.isEmpty()) {
            val svc = ((AbstractRegisteredService) service).newInstance();
            svc.setId(service.getId());
            return svc;
        }
        return (RegisteredService) shadows.get(0).get();
    }
}
