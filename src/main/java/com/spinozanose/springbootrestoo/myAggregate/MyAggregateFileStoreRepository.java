package com.spinozanose.springbootrestoo.myAggregate;

import java.util.Map;

/**
 * Not implemented. We are pretending that we will be saving to the local file system. Of course,
 * we could swap out an iplementation for MongoDb, a Database, or S3, or anything else.
 */
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
