package com.spinozanose.springbootrestoo.myAggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAggregateSearchServiceMock implements MyAggregateSearchService {

    final List<String> ids = new ArrayList<>();

    @Override
    public List<String> search(Map<String, String> searchParams) {
        return ids;
    }
}
