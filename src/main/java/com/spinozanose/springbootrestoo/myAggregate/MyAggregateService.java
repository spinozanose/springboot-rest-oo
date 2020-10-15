package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.implementation.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.implementation.MyUUID;
import com.spinozanose.springbootrestoo.implementation.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * This service is for managing the aggregate and its encapsulated objects. It
 * specifically manages the application transactions, which includes creation and
 * persistence of objects.
 *
 * It throws application exceptions that must be handled by the controller when there
 * is something wrong.
 *
 * It does not know or speak JSON or anything else from the Controller, and it has no
 * domain logic, either. In particular it does not know the content of the DTOs. It
 * absolutely does no validation!
 *
 * Notice that there are five operations (methods) for data management because RESTful
 * applications are data providers. If this were an event-driven system the methods
 * would be named after the commands, and would issue named events to the repository.
 *
 * Notice that the framework influence (SpringBoot in this case) ends here. Springboot
 * is a very intrusive framework but it simplifies the RESTful web layer and managing
 * enterprise capabilities. It is very helpful but we do not want it in our domain
 * model. As Uncle Bob puts it, frameworks are details, too.
 *
 * If creational logic is complex it should be delegated to a named factory. And do not
 * use anything like a Builder Pattern! That is a violation of good OO practice: the object
 * owns its data, and manages it. Data passes through the controller and the service as a DTO.
 */
@Service
class MyAggregateService {

    // default scope and non-final so it can be replaced in test code
    // we are pretending that in the final version of this application we are using Elasticsearch for
    // search and the file system for persistence.
    MyAggregateRepository repository = new MyAggregateFileStoreRepository();
    MyAggregateSearchService searchService = new MyAggregateElasticSearchService();

    public List<String> search(final Map<String, String> searchParams) throws IllegalArgumentException {
        // Here we can check a cache or any other optimizations
        return searchService.search(searchParams);
    }

    /**
     * Note that we have decided that assigning the id a value is done in the service and the domain knows
     * and cares nothing about it. But it is also fine to have the domain object assign it for itself, or
     * to use a service. When using a relational database this is likely defined there as a sequential
     * integer so it does not have to be handled here at all.
     *
     * It is usually best not to allow clients to assign unique ids, as it is often hard to make sure that
     * they are truly unique. Assuring uniqueness has to be guaranteed here, anyway, so if we are not
     * assigning the id we would have to validate it.
     *
     * @param data
     * @return MyAggregate
     * @throws InvalidDomainDataException
     */
    public MyAggregate create(final Map<String, Object> data) throws InvalidDomainDataException {

        if (data.get("id") != null) {
            throw new InvalidDomainDataException("Do not specify an id when creating a new MyAggregateRoot!");
        }
        // create id
        data.put("id", new MyUUID().toString());
        MyAggregateRoot newMyAggregateRoot = new MyAggregateRoot(data, repository);
        newMyAggregateRoot.persist();
        return newMyAggregateRoot;
    }

    public MyAggregate read(final String id) {
        final Map<String, Object> data = repository.read(id);
        if (data == null) return null;
        try {
            return new MyAggregateRoot(data, repository);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen as all data in the repository is valid! Right?");
        }
    }

    public void update(final Map<String, Object> data) throws ObjectNotFoundException, InvalidDomainDataException {
        final String id = (String) data.get("id");
        if (id == null) throw new ObjectNotFoundException("No id specified!");
        final Map<String, Object> existingData = repository.read(id);
        if (data == null) {
            throw new ObjectNotFoundException("No MyAggregate to update with id " + id);
        }
        final MyAggregateRoot myAggregateRoot;
        try {
            myAggregateRoot = new MyAggregateRoot(existingData, repository);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException("This should not happen unless the data in the repository is invalid!",e);
        }
        myAggregateRoot.update(data);
        myAggregateRoot.persist();
    }

    /**
     *
     * This example implementation is of an actual delete. But you could have a logical delete. In
     * that case there is a delete flag on the MyAggregateRoot object, and managing a delete is the
     * same as a update. The read operation could then check to make sure it has not been deleted
     * if we do not pass deleted objects back to the clients.
     *
     * @param id
     * @throws ObjectNotFoundException
     */
    public void delete(final String id) throws ObjectNotFoundException {
        final Map<String, Object> data = repository.read(id);
        if (data == null) {
            throw new ObjectNotFoundException("No MyAggregate with id " + id);
        } else {
            repository.delete(id);
        }
    }
}
