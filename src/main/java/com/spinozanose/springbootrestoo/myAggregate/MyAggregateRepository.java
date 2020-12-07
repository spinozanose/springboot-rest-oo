package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.DomainPersistenceException;

import java.util.Map;

/**
 * We use an interface primarily so it can be mocked for testing.
 *
 * The choice to have a MyAggregateRepository instead of a generic Repository for the
 * entire application is arbitrary. Either would be fine, and in a real application
 * you could have an inheritance hierarchy with shared functionality in a base class.
 *
 * Notice that this is an anti-corruption layer.
 *
 * The DomainPersistenceException should wrap any checked exceptions the implementing
 * service throws.
 */
interface MyAggregateRepository {

    void write(Map<String, Object> data) throws DomainPersistenceException;

    Map<String, Object> read(String id) throws DomainPersistenceException;

    void delete(String id) throws DomainPersistenceException;
}
