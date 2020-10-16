package com.spinozanose.springbootrestoo.myAggregate;

import java.util.List;
import java.util.Map;

/**
 * Not implemented. We are pretending that we will do searching in Elasticsearch.
 */
class MyAggregateElasticSearchService implements MyAggregateSearchService {

    public List<String> search(final Map<String, String> searchParameters) {
        throw new UnsupportedOperationException();
    }
}
