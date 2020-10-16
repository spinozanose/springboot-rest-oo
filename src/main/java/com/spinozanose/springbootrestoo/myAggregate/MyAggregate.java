package com.spinozanose.springbootrestoo.myAggregate;

import java.util.Map;

/**
 * While not strictly necessary, this interface allows a clean separation between the
 * MyAggregate domain concept from the MyAggregateRoot implementation. The root of an
 * aggregate is the entry point for all operations within the aggregate's context, but
 * some operations probably should not be shared outside of the package boundary. This
 * ensures that.
 */
public interface MyAggregate {
    
    Map<String, Object> toMap();

}
