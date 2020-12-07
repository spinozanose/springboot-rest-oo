package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;

import java.util.Map;

/**
 * Not yet implemented
 */
class MyAggregateUpdateValidator {

    public void validityCheck(Map<String, Object> data) throws InvalidDomainDataException {
        this.validityCheck(new MyAggregateDto(data));
    }

    public void validityCheck(MyAggregateDto dto) throws InvalidDomainDataException {
        // specific data validation rules can be here.
    }
}
