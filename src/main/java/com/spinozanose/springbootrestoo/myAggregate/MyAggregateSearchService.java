package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;

import java.util.List;
import java.util.Map;

interface MyAggregateSearchService {
    List<String> search(Map<String, String> searchParams) throws InvalidSearchParametersException;
}
