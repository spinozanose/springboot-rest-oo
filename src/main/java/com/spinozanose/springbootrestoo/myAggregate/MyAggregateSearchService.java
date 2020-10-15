package com.spinozanose.springbootrestoo.myAggregate;

import java.util.List;
import java.util.Map;

public interface MyAggregateSearchService {
    List<String> search(Map<String, String> searchParams);
}
