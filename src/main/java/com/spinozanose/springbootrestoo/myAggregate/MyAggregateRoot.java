package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class MyAggregateRoot implements MyAggregate{

    private final String id;
    private int aNumber;
    private String aString;
    private InnerObject innerObject;
    private final MyAggregateRepository repository;

    MyAggregateRoot(Map<String, Object> data, MyAggregateRepository repository) throws InvalidDomainDataException {
        if (!isValid(data)) {
            throw new InvalidDomainDataException("Cannot create new MyAggregateRoot with invalid data!");
        }
        this.repository = repository;
        this.id = (String) data.get("id");
        this.aNumber = (int) data.get("aNumber");
        this.aString = (String) data.get("aString");
        final Map<String, Object> innerObjectData = (Map<String, Object>) data.get("innerObject");
        if (innerObjectData != null) this.innerObject = new InnerObject(innerObjectData);
    }

    public void update(Map<String, Object> data) throws InvalidDomainDataException {
        if (MyAggregateRoot.isValid(data)) {
            this.aNumber = (int) data.get("aNumber");
            this.aString = (String) data.get("aString");
            this.innerObject = new InnerObject((Map<String, Object>) data.get("innerObject"));
        } else {
            throw new InvalidDomainDataException("Update not allowed! Data for update is not valid.");
        }
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> data = new HashMap<>();
        data.put("id", this.id);
        data.put("aNumber", this.aNumber);
        data.put("aString", this.aString);
        if (this.innerObject != null) {
            data.put("innerObject", this.innerObject.toMap());
        }
        return Collections.unmodifiableMap(data);
    }

    void persist() {
        repository.write(this.toMap());
    }

    private static boolean isValid(Map<String, Object> data) {
        // Validate here . . .
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyAggregateRoot that = (MyAggregateRoot) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



