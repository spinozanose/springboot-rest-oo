package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.exceptions.InvalidSearchParametersException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
import com.spinozanose.springbootrestoo.email.EmailSendingService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Because we have decoupled from Spring in the Service class, the testing
 * has been simplified. Here we just instantiate the factory, swap out the
 * dependent services with mocks or nulls, and then call the methods. Notice that
 * the design of the mock is like the way you use Mockito: you set the response
 * that you want the service to return. You can also test that the arguments passed
 * into the mock are what are expected.
 *
 * Easy peasy. Straightforward Java, and no messing with the Application Context or
 * trying to work around classpath scanning!
 */
@SpringBootTest
public class MyAggregateFactoryTest {

    private static final String TEST_ID = "testId";
    private static final MyAggregateRoot TEST_AGGREGATE;
    static {
        final MyAggregateRepository repository = null;
        final EmailSendingService emailer = null;
        final Map<String, Object> data = new HashMap<>();
        data.put("id", TEST_ID);
        data.put("aNumber", 23);
        try {
            TEST_AGGREGATE = new MyAggregateRoot(data, repository, emailer);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnListOfIdsOnValidSearch() throws InvalidSearchParametersException {
        // right now there is no real logic because we have not decided
        // on an implementation. The service result just passes through.
        final String testId = "searchServiceTestId";
        final MyAggregateFactory factory = new MyAggregateFactory();
        //
        final MockMyAggregateSearchService searchService = new MockMyAggregateSearchService();
        // set up service for testing
        factory.repository = null;
        factory.searchService = searchService;
        //
        searchService.ids.add(testId);
        final Map<String, String> searchParameters = null;
        final List<String> returnedIds = factory.search(searchParameters);
        //
        final List<String> expectedIds = new ArrayList<>();
        expectedIds.add(testId);
        assertEquals(expectedIds, returnedIds);
    }

    @Test
    public void shouldThrowExceptionOnCreateWithId() {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        factory.repository = new MockMyAggregateRepository();
        //
        final Map<String,Object> data = TEST_AGGREGATE.toMap();
        boolean exceptionThrown = false;
        try {
            final MyAggregate createdMyAggregate = factory.create(data);
        } catch (InvalidDomainDataException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void shouldPersistAndReturnNewAggregateOnCreate() throws InvalidDomainDataException {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        MockMyAggregateRepository mockRepository = new MockMyAggregateRepository();
        factory.repository = mockRepository;
        //
        // we have to copy the map because it is unmodifiable.
        final Map<String,Object> data = new HashMap<>(TEST_AGGREGATE.toMap());
        data.remove("id");
        final MyAggregate createdMyAggregate = factory.create(data);
        assertNotEquals(createdMyAggregate.toMap().get("id"), TEST_AGGREGATE.toMap().get("id"));
        assertEquals(createdMyAggregate.toMap(), mockRepository.object);
    }

    @Test
    public void shouldReturnMyAggregateOnRead() {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        final MockMyAggregateRepository mockRepository = new MockMyAggregateRepository();
        mockRepository.object = TEST_AGGREGATE.toMap();
        factory.repository = mockRepository;
        //
        final MyAggregate returnedMyAggregate = factory.read(TEST_ID);
        // we compare the data since the equals() method only considers id
        assertEquals(TEST_AGGREGATE.toMap(), returnedMyAggregate.toMap());
    }

    @Test
    public void shouldChangeMyAggregateOnUpdate() throws InvalidDomainDataException, ObjectNotFoundException {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        final MockMyAggregateRepository mockRepository = new MockMyAggregateRepository();
        mockRepository.object = TEST_AGGREGATE.toMap();
        factory.repository = mockRepository;
        //
        final Map<String, Object> newData = new HashMap<>(TEST_AGGREGATE.toMap());
        final String newString = "Bananas don't fly!";
        newData.put("aString", newString);
        factory.update(newData);
        // we compare the data since the equals() method only considers id
        assertNotEquals(TEST_AGGREGATE.toMap(), mockRepository.object);
        assertEquals(newString, mockRepository.object.get("aString"));
    }

    @Test
    public void shouldRemoveMyAggregateOnDelete() throws ObjectNotFoundException {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        final MockMyAggregateRepository mockRepository = new MockMyAggregateRepository();
        mockRepository.object = TEST_AGGREGATE.toMap();
        factory.repository = mockRepository;
        //
        factory.delete(TEST_ID);
        //
        assertEquals(TEST_ID, mockRepository.deletedId);
    }

    @Test
    public void shouldReturnErrorIfIdNotFoundOnDelete() {
        final MyAggregateFactory factory = new MyAggregateFactory();
        factory.searchService = null;
        final MockMyAggregateRepository mockRepository = new MockMyAggregateRepository();
        mockRepository.object = TEST_AGGREGATE.toMap();
        factory.repository = mockRepository;
        //
        boolean errorThrown = false;
        try {
            factory.delete("nonexistent id");
        } catch (ObjectNotFoundException e) {
            errorThrown = true;
        }
        //
        assertTrue(errorThrown);
        assertEquals("", mockRepository.deletedId);
    }

}
