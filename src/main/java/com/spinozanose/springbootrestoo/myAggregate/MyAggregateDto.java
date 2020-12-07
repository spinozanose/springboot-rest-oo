package com.spinozanose.springbootrestoo.myAggregate;

import java.util.Map;

public class MyAggregateDto {

    public static final String ID = "id";
    public static final String A_NUMBER = "aNumber";
    public static final String A_STRING = "aString";
    public static final String INNER_OBJECT = "innerObject";
    public static final String[] FIELDS = {ID, A_NUMBER, A_STRING, INNER_OBJECT};

    public final String id;
    // Integer instead of int because aNumber is allowed to be null here
    public final Integer aNumber;
    public final String aString;
    public final InnerObject innerObject;

    /**
     * One of the nice benefits of having this DTO is that the casting from the Map is done here.
     *
     * There is an implicit null check for the data object.
     *
     * @param data
     */
    public MyAggregateDto(final Map<String, Object> data) {
        id = (String) data.get(ID);
        aNumber = (Integer) data.get(A_NUMBER);
        aString = (String) data.get(A_STRING);
        final Map<String, Object> innerObjectData = (Map<String, Object>) data.get("innerObject");
        this.innerObject = (innerObjectData == null) ? null : new InnerObject(innerObjectData);
    }
}
