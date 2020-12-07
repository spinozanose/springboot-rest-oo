package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.MyUUID;
import com.spinozanose.springbootrestoo.common.exceptions.DomainPersistenceException;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
import com.spinozanose.springbootrestoo.email.EmailSendingService;

import java.util.List;
import java.util.Map;

class MyAggregateFactory {

    /**
     * Services are default scope and non-final so they can be replaced in test code.
     * <p>
     * We are pretending that in the final version of this application we are using
     * Elasticsearch for search and the file system for persistence.
     * <p>
     * Notice that there are lots of options here. These could be lazy-loaded singletons
     * or calls to factories that set the services up with caches or other neat additions.
     * They can spawn jobs and return immediately if the service's operation(s) are not
     * required to be in the application transaction. And all nicely decoupled.
     */
    MyAggregateRepository repository = new MyAggregateFileStoreRepository();
    MyAggregateSearchService searchService = new MyAggregateElasticSearchService();
    EmailSendingService emailSendingService = new EmailSendingService();

    /**
     * In this implementation we return a list of MyAggregate objects in JSON (passed through). We could
     * have also returned MyAggregate objects themselves if we wanted to instantiate them.
     *
     * @param searchParams
     * @return
     * @throws InvalidSearchParametersException
     */
    List<String> search(final Map<String, String> searchParams) throws InvalidSearchParametersException {
        return searchService.search(searchParams);
    }

    /**
     * Note that we have decided that assigning the id a value is done in the factory and the domain knows
     * and cares nothing about it. But it is also fine to have the domain object assign it for itself, or
     * to use a service. When using a relational database ids are most often set inside the database with a
     * sequential integer so it does not have to be handled here at all.
     * <p>
     * It is usually best not to allow clients to assign unique ids, so we forbid it here. The reason it is not
     * great is because it can lead to difficult problems to solve, or else the most important characteristic of
     * a id, its uniqueness, can be lost. Basically, it is often hard to make sure that ids are truly unique
     * unless they are all created in one place.
     * <p>
     * For some reason we send an email to someone when a MyAggregate is created. I'm not sure why you would do
     * this, but for now just recognize it as an example of a service that should be set into the AggregateRoot
     * object because only the aggregate root itself has access to its aggregate root data.
     *
     * @param data
     * @return MyAggregate
     * @throws InvalidDomainDataException
     * @throws DomainPersistenceException
     */
    MyAggregate create(final Map<String, Object> data) throws InvalidDomainDataException, DomainPersistenceException {

        if (data.get(MyAggregateDto.ID) != null) {
            /**
             * This seems less surprising and less magical than accepting the passed id or replacing it
             * with a different one.
             */
            throw new InvalidDomainDataException("Do not specify an id when creating a new MyAggregateRoot!");
        }
        // create and add id
        data.put(MyAggregateDto.ID, new MyUUID().toString());
        // wrap in dto
        final MyAggregateDto dto = new MyAggregateDto(data);
        // validate
        final MyAggregateNewValidator validator = new MyAggregateNewValidator();
        // throws exception on invalid data
        validator.validityCheck(dto);
        // create MyAggregateRoot
        final MyAggregateRoot myAggregateRoot = new MyAggregateRoot(dto, repository, emailSendingService);
        // throws exception on persistence failure. This could be a crappy user experience, though.
        myAggregateRoot.persist();
        // again, this could be a crappy user experience. So other implementations can be considered.
        myAggregateRoot.sendEmail();
        // note that the object is only returned if all the services are without error. No rollback required.
        return myAggregateRoot;
    }

    /**
     *
     * @param id
     * @return MyAggregate
     * @throws DomainPersistenceException
     */
    MyAggregate read(final String id) throws DomainPersistenceException {
        final Map<String, Object> data = repository.read(id);
        if (data == null) return null;
        MyAggregateDto dto = new MyAggregateDto(data);
        try {
            return new MyAggregateRoot(dto, repository, emailSendingService);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen as all data in the repository should be valid! Right?");
        }
    }

    /**
     * @param updateData
     * @throws ObjectNotFoundException
     * @throws InvalidDomainDataException
     */
    void update(final Map<String, Object> updateData) throws ObjectNotFoundException, InvalidDomainDataException, DomainPersistenceException {
        // convert to DTO
        final MyAggregateDto updateDataDto = new MyAggregateDto(updateData);
        // get existing data
        final String id = updateDataDto.id;
        if (id == null) throw new ObjectNotFoundException("No id specified!");
        final Map<String, Object> existingData = repository.read(id);
        if (existingData == null) {
            throw new ObjectNotFoundException("No MyAggregate to update with id " + id);
        }
        // wrap in DTO
        final MyAggregateDto existingDataDto = new MyAggregateDto(existingData);
        // instantiate myAggregateData
        final MyAggregateRoot myAggregateRoot;
        try {
            myAggregateRoot = new MyAggregateRoot(existingDataDto, repository, emailSendingService);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen unless the data in the repository is invalid!", e);
        }
        // update
        myAggregateRoot.update(updateDataDto);
        // persist
        myAggregateRoot.persist();
    }

    /**
     * This example implementation is of an actual delete. But you could have a logical delete. In
     * that case there is a delete flag on the MyAggregateRoot object, and managing a delete is the
     * same as a update. The read operation could then check to make sure it has not been deleted
     * if we do not pass deleted objects back to the clients.
     *
     * @param id
     * @throws ObjectNotFoundException
     * @throws DomainPersistenceException
     */
    void delete(final String id) throws ObjectNotFoundException, DomainPersistenceException {
        final Map<String, Object> data = repository.read(id);
        if (data == null) {
            throw new ObjectNotFoundException("No MyAggregate with id " + id);
        } else {
            repository.delete(id);
        }
    }
}
