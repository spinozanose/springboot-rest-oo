package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.MyUUID;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * This service is for managing the aggregate and its encapsulated objects. It
 * specifically manages the application transactions, which includes creation and
 * persistence of objects. It throws application exceptions that must be handled by
 * the controller when there is something wrong.
 *
 * It does not know or speak JSON or anything else from the Controller, and it has no
 * domain logic, either. In particular it does not know the content of the DTOs. It
 * absolutely does no validation!
 *
 * In the usual OO implementation this would be called the controller, and might be combined
 * with the functionality for managing the client interface. But since we are taking advantage
 * of the Springboot service injection functionality, and the mocking of it in the tests, we
 * call this a service.
 *
 * Notice that there are five operations for data management because RESTful
 * applications are data providers. If this were an event-driven system the methods
 * would be named after the commands, and would issue named events to the repository.
 *
 * Notice that the framework influence (SpringBoot in this case) ends here. Springboot
 * is a very intrusive framework but it simplifies the RESTful web layer and managing
 * the enterprise capabilities. It is very helpful but we do not want it in our domain
 * model. As Uncle Bob puts it, frameworks are details, too.
 */
@Service
class MyAggregateService {

    //package-private so it can be mocked in tests
    MyAggregateFactory factory = new MyAggregateFactory();

    List<String> search(final Map<String, String> searchParams) throws InvalidSearchParametersException {
        // Here we can check a cache or any other optimizations
        return factory.search(searchParams);
    }

    MyAggregate create(final Map<String, Object> data) throws InvalidDomainDataException {
        return factory.create(data);
    }

    MyAggregate read(final String id) {
        return factory.read(id);
    }

    void update(final Map<String, Object> data) throws ObjectNotFoundException, InvalidDomainDataException {
        factory.update(data);
    }

    void delete(final String id) throws ObjectNotFoundException {
        factory.delete(id);
    }
}
