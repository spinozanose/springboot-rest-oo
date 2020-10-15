package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;

import java.util.Map;

public class MyAggregateFileStoreRepository implements MyAggregateRepository {

    @Override
    public void write(Map<String, Object> data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> read(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException();
    }
}
