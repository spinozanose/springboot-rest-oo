package com.spinozanose.springbootrestoo.myAggregate;

import java.util.Map;

/**
 * Notice the scoping. Creation is constrained to the aggregate, but it may be viewed everywhere.
 *
 * Not implemented
 */
public class InnerObject {

    InnerObject(Map<String, Object> data) {
    }

    /**
     * This should return an immutable view of the object.
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> toMap() {
        return null;
    }
}
