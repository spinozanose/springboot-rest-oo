package com.spinozanose.springbootrestoo.myAggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Here we define what we want the search to return.
 */
public class MockMyAggregateSearchService implements MyAggregateSearchService {

    final List<String> myAggregatesObjects = new ArrayList<>();

    @Override
    public List<String> search(Map<String, String> searchParams) {
        return myAggregatesObjects;
    }
}
