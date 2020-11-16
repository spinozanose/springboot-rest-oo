package com.spinozanose.springbootrestoo.myAggregate;

import java.util.List;
import java.util.Map;

/**
 * Not implemented. We are pretending that we will do searching in Elasticsearch.
 *
 * While this is named as part of the MyAggregate aggregate, it is more likely that
 * this is a lightweight implementation / extension of a base class that does most
 * of the heavy lifting for communicating to the service. Here, though, is likely
 * where validation tied to this particular use case would occur.
 */
class MyAggregateElasticSearchService implements MyAggregateSearchService {

    public List<String> search(final Map<String, String> searchParameters) {
        throw new UnsupportedOperationException();
    }
}
