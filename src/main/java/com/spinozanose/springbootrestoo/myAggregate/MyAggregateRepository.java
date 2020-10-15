package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;

import java.util.Map;

interface MyAggregateRepository {

    void write(Map<String, Object> data);

    Map<String, Object> read(String id);

    void delete(String id);
}
