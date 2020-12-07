package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;

import java.util.Map;

/**
 * This implementation presumes that the update implementation is to provide data that is
 * changed, and not provide unchanged data. That may not be the implementation, of course,
 * but this is a cute way to handle both the new and update use case.
 */
class MyAggregateNewValidator extends MyAggregateUpdateValidator {

    @Override
    public void validityCheck(Map<String, Object> data) throws InvalidDomainDataException {
        throwErrorOnNullData(MyAggregateDto.ID, data);
        throwErrorOnNullData(MyAggregateDto.A_NUMBER, data);
        throwErrorOnNullData(MyAggregateDto.A_STRING, data);
        throwErrorOnNullData(MyAggregateDto.INNER_OBJECT, data);
        super.validityCheck(data);
    }

    private static void throwErrorOnNullData(final String name, Map<String, Object> data) throws InvalidDomainDataException {
        if (data == null) throw new InvalidDomainDataException("MyAggregate data is null!");
        if (data.get(name) == null) {
            throw new InvalidDomainDataException("MyAggregate field " + name + " is null!");
        }
    }
}
