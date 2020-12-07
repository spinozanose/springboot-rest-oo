package com.spinozanose.springbootrestoo.myAggregate;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a mock for use in the tests of the MyAggregateService.
 */
public class MockMyAggregateRepository implements MyAggregateRepository {

    Map<String, Object> object = new HashMap<>();

    String deletedId = "";

    @Override
    public void write(Map<String, Object> data) {
        this.object = data;
    }

    @Override
    public Map<String, Object> read(String id) {
        if (object.get(MyAggregateDto.ID).equals(id)) {
            return object;
        } else {
            return null;
        }
    }

    @Override
    public void delete(String id) {
        this.deletedId = id;
    }
}
