package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.DomainPersistenceException;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.email.EmailMessage;
import com.spinozanose.springbootrestoo.email.EmailSendingService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
 * The operations are named in this class according to the needs of the RESTful interface, i.e. CRUD
 * data operations. However, in most other cases the naming of the operation is from the requirements,
 * and stated as an imperative. You know it is an imperative because you should add "please" to be
 * polite.
 *
 * Notice the arguments of the methods. There are no getters or setters. The data enters
 * or leaves the aggregate only as a DTO (Data Transfer Object), in this case a Map<String, Object>
 * or a MyAggregateDto type. This architectural constraint ensures that all logic associated with
 * the aggregate must occur inside the aggregate boundary. It also ensures that the intermediary
 * layers, the controllers, handlers, and network interfaces can all be ignorant of the data, and
 * therefore simpler and unentangled. Nothing is more entangling than data.
 *
 * Also notice that the return from the methods are either valid objects or exceptions. This is
 * the appropriate use of exceptions, to indicate that an operation is not going to be completed as
 * expected. Just like the constructor, command methods result in a valid changed object or an
 * exception. Query methods (in this case just the toMap() methods) have no side effects, i.e., they
 * do not change the state of the object. This principle is called CQS, Command Query Segregation.
 */
class MyAggregateRoot implements MyAggregate{

    private final String id;
    private int aNumber;
    private String aString;
    private InnerObject innerObject;

    private final MyAggregateRepository repository;
    private final EmailSendingService emailSendingService;

    /**
     *
     * @param dto
     * @param repository
     * @param emailSendingService
     * @throws InvalidDomainDataException
     */
    MyAggregateRoot(final MyAggregateDto dto, final MyAggregateRepository repository, final EmailSendingService emailSendingService) throws InvalidDomainDataException {
        this.repository = repository;
        this.emailSendingService = emailSendingService;
        this.id = dto.id;
        this.aNumber = dto.aNumber;
        this.aString = dto.aString;
        this.innerObject = dto.innerObject;
    }

    /**
     * Not all the data needs to be returned, even if in this case we do. We only should return data
     * that is appropriate to share.
     *
     * @return Map<String, Object>
     */
    public Map<String, Object> toMap() {
        final Map<String, Object> data = new HashMap<>();
        data.put(MyAggregateDto.ID, this.id);
        data.put(MyAggregateDto.A_NUMBER, this.aNumber);
        data.put(MyAggregateDto.A_STRING, this.aString);
        if (this.innerObject != null) {
            data.put(MyAggregateDto.INNER_OBJECT, this.innerObject.toMap());
        }
        return Collections.unmodifiableMap(data);
    }

    /**
     * @return MyAggregateDto
     */
    public MyAggregateDto toDto() {
        return new MyAggregateDto(toMap());
    }

    void update(final MyAggregateDto dto) throws InvalidDomainDataException {
        // validate before state change
        final MyAggregateUpdateValidator validator = new MyAggregateUpdateValidator();
        // throws exception if invalid
        validator.validityCheck(dto);
        // update
        if (notNull(dto.aNumber)) {
            this.aNumber = dto.aNumber;
        }
        if (notNull(dto.aString)) {
            this.aString = dto.aString;
        }
        if (notNull(dto.innerObject)) {
            this.innerObject = dto.innerObject;
        }
    }

    void persist() throws DomainPersistenceException {
        repository.write(this.toMap());
    }

    void sendEmail() {
        // here we set the values of the email message based on data in the aggregate.
        // or, well, we would if we actually implemented it.
        final String sender = null;
        final String[] recipients = null;
        final String subject = null;
        final String message = null;
        EmailMessage email = new EmailMessage(sender, recipients, subject, message);
        emailSendingService.sendEmail(email);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyAggregateRoot that = (MyAggregateRoot) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private static boolean notNull(final Object object) {
        return object != null;
    }

}



