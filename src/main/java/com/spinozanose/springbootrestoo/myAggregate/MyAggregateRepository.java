package com.spinozanose.springbootrestoo.myAggregate;

import java.util.Map;

/**
 * We use an interface primarily so it can be mocked for testing.
 *
 * The choice to have a MyAggregateRepository instead of a generic Repository for the
 * entire application is arbitrary. Either would be fine, and in a real application
 * you probably would have an inheritance hierarchy with shared functionality in the
 * base class.
 */
interface MyAggregateRepository {

    void write(Map<String, Object> data);

    Map<String, Object> read(String id);

    void delete(String id);
}
