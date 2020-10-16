package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Note the scopes! These are intentional. In OO we do not make everything public. We hide
 * methods and (particularly!) data. This may seem odd to many new to OO.
 *
 * For this class we use the default scope, also known as package-private. There is a reason
 * why the default for Java, an OO language, is package-private. It is the most natural of
 * the scopes for OO.
 *
 * This is the root entity of the MyAggregate aggregate (good naming, huh?). All of the
 * operations on the aggregate, or any part of the aggregate, happen here.
 *
 * In OO, objects can always be trusted to be in valid state. One way of enforcing this constraint
 * is to use constructors that require that data and other dependencies that are required for their
 * creation will result in a valid object, or else the constructor will throw an exception and the
 * object is not created. So in OO designs, no-arg constructors are rare for entities and required
 * for value objects.
 *
 * Notice the contract for the data in the methods. There are no getters or setters. The data enters
 * or leaves the aggregate only as a DTO (Data Transfer Object), in this case a Map<String, Object>.
 * This architectural constraint ensures that all logic associated with the aggregate must occur
 * inside the aggregate boundary. It also ensures that the intermediary layers, the controllers,
 * handlers, and network interfaces can all be ignorant of the data, and therefore simpler and
 * unentangled.
 *
 * Also notice that the return from the methods are either "valid" responses or exceptions. This is
 * the appropriate use of exceptions, to indicate that an operation is not going as expected. Just
 * like the constructor, command methods result in a valid changed object or an exception. Query
 * methods (in this case just the toMap() method) have no side effects, i.e., they do not change the
 * state of the object. This principle is called CQS, Command Query Segregation, and in OO we do that.
 *
 */
class MyAggregateRoot implements MyAggregate{

    private final String id;
    private int aNumber;
    private String aString;
    private InnerObject innerObject;
    private final MyAggregateRepository repository;

    MyAggregateRoot(Map<String, Object> data, MyAggregateRepository repository) throws InvalidDomainDataException {
        if (!isValid(data)) {
            throw new InvalidDomainDataException("Cannot create new MyAggregate with invalid data!");
        }
        this.repository = repository;
        this.id = (String) data.get("id");
        this.aNumber = (int) data.get("aNumber");
        this.aString = (String) data.get("aString");
        final Map<String, Object> innerObjectData = (Map<String, Object>) data.get("innerObject");
        if (innerObjectData != null) this.innerObject = new InnerObject(innerObjectData);
    }

    void update(Map<String, Object> data) throws InvalidDomainDataException {
        if (MyAggregateRoot.isValid(data)) {
            this.aNumber = (int) data.get("aNumber");
            this.aString = (String) data.get("aString");
            this.innerObject = new InnerObject((Map<String, Object>) data.get("innerObject"));
        } else {
            throw new InvalidDomainDataException("Update not allowed! Data for update is not valid.");
        }
    }

    /**
     * Not all the data needs to be returned, even in this case we do. We only should return data
     * that is appropriate to share.
     *
     * @return Map<String, Object>
     */
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

    /**
     * For testing, this can be made package-private.
     *
     * @param data
     * @return boolean
     */
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



