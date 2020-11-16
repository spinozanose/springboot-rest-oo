package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;

import java.util.List;
import java.util.Map;

/**
 * This interface is for decoupling the domain model from the search implementation choice. This
 * is a classic case of what is known as "interface programming." Not only does this interface
 * decouple the model, it makes it much easier to test with a service stub.
 */
interface MyAggregateSearchService {
    List<String> search(Map<String, String> searchParams) throws InvalidSearchParametersException;
}
