package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.MyUUID;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
import com.spinozanose.springbootrestoo.email.EmailSendingService;

import java.util.List;
import java.util.Map;

/**
 * This is the interface to operations that retrieve instances of MyAggregate.
 */
class MyAggregateFactory {

    /**
     * Services are default scope and non-final so they can be replaced in test code.
     * <p>
     * We are pretending that in the final version of this application we are using Elasticsearch for
     * search and the file system for persistence.
     * <p>
     * Notice that there are lots of options here. These could be lazy-loaded singletons
     * or calls to factories that set them up with caches or other neat additions. They
     * can spawn jobs and return immediately if they are not required to be in the
     * transaction. And all nicely decoupled.
     */
    MyAggregateRepository repository = new MyAggregateFileStoreRepository();
    MyAggregateSearchService searchService = new MyAggregateElasticSearchService();
    EmailSendingService emailSendingService = new EmailSendingService();

    List<String> search(final Map<String, String> searchParams) throws InvalidSearchParametersException {
        // Here we can check a cache or do any other optimizations
        return searchService.search(searchParams);
    }

    /**
     * Note that we have decided that assigning the id a value is done in the factory and the domain knows
     * and cares nothing about it. But it is also fine to have the domain object assign it for itself, or
     * to use a service. When using a relational database this is likely defined there as a sequential
     * integer so it does not have to be handled here at all.
     * <p>
     * It is usually best not to allow clients to assign unique ids, so we forbid it here. It is often hard
     * to make sure that ids are truly unique unless done in one place. Assuring uniqueness has to be
     * guaranteed in this method, anyway, so even if we were not assigning the id we would have to validate it.
     * <p>
     * For some reason we send an email to someone when a MyAggregate created. I'm not sure why you would do this,
     * but for now just recognize it is an example of a service that should be set into the AggregateRoot object
     * because only the AggregateRoot itself has access to its data.
     *
     * @param data
     * @return MyAggregate
     * @throws InvalidDomainDataException
     */
    MyAggregate create(final Map<String, Object> data) throws InvalidDomainDataException {

        if (data.get("id") != null) {
            /**
             * This seems less surprising and less magical than accepting the passed id or replacing it
             *  with a different one.
             */
            throw new InvalidDomainDataException("Do not specify an id when creating a new MyAggregateRoot!");
        }
        // create id
        data.put("id", new MyUUID().toString());
        MyAggregateRoot newMyAggregateRoot = new MyAggregateRoot(data, repository, emailSendingService);
        newMyAggregateRoot.persist();
        newMyAggregateRoot.sendEmail();
        return newMyAggregateRoot;
    }

    MyAggregate read(final String id) {
        final Map<String, Object> data = repository.read(id);
        if (data == null) return null;
        try {
            return new MyAggregateRoot(data, repository, emailSendingService);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen as all data in the repository should be valid! Right?");
        }
    }

    /**
     * @param data
     * @throws ObjectNotFoundException
     * @throws InvalidDomainDataException
     */
    void update(final Map<String, Object> data) throws ObjectNotFoundException, InvalidDomainDataException {
        final String id = (String) data.get("id");
        if (id == null) throw new ObjectNotFoundException("No id specified!");
        final Map<String, Object> existingData = repository.read(id);
        if (data == null) {
            throw new ObjectNotFoundException("No MyAggregate to update with id " + id);
        }
        final MyAggregateRoot myAggregateRoot;
        try {
            myAggregateRoot = new MyAggregateRoot(existingData, repository, emailSendingService);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen unless the data in the repository is invalid!", e);
        }
        myAggregateRoot.update(data);
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
     */
    void delete(final String id) throws ObjectNotFoundException {
        final Map<String, Object> data = repository.read(id);
        if (data == null) {
            throw new ObjectNotFoundException("No MyAggregate with id " + id);
        } else {
            repository.delete(id);
        }
    }
}
