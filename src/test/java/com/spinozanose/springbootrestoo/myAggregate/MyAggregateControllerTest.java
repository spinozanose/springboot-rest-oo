package com.spinozanose.springbootrestoo.myAggregate;

import com.spinozanose.springbootrestoo.common.exceptions.InvalidDomainDataException;
import com.spinozanose.springbootrestoo.common.exceptions.ObjectNotFoundException;
import com.spinozanose.springbootrestoo.email.EmailSendingService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * These are tests of the controller, and the interface to the controller. To do this we use
 * the Spring WebMvcTest capability. We mock the service (with Mockito) to make sure we are
 * only testing the controller.
 *
 * This is not a complete set of tests. We may want to test lots more things, like Content-Type
 * and JSON parsing and the error handling.
 */
@WebMvcTest(MyAggregateController.class)
public class MyAggregateControllerTest {

    private static final String TEST_ID = "testId";
    private static final MyAggregateRoot TEST_AGGREGATE;
    static {
        final MyAggregateRepository dao = null;
        final EmailSendingService emailer = null;
        final Map<String, Object> data = new HashMap<>();
        data.put(MyAggregateDto.ID, TEST_ID);
        data.put(MyAggregateDto.A_NUMBER, 23);
        final MyAggregateDto dto = new MyAggregateDto(data);
        try {
            TEST_AGGREGATE = new MyAggregateRoot(dto, dao, emailer);
        } catch (InvalidDomainDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyAggregateService service;

    @Test
    public void shouldReturn201AndObjectOnCreate() throws Exception {
        // create mock of service
        when(service.create(anyMap())).thenReturn(TEST_AGGREGATE);
        // set up data to be passed into controller (without id)
        final JSONObject jsonObject = new JSONObject(TEST_AGGREGATE.toMap());
        final String testAggregateJsonString= jsonObject.toJSONString();
        // and expect a URI in the Location header
        this.mockMvc.perform(
                post("/v1/myaggregate")
                        .contentType("application/json")
                        .content(testAggregateJsonString))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("Location", equalTo("/v1/myaggregate/testId")));
    }

    @Test
    public void shouldReturn200ForReadWithExistingId() throws Exception {
        //
        when(service.read(TEST_ID)).thenReturn(TEST_AGGREGATE);
        //
        final JSONObject jsonObject = new JSONObject(TEST_AGGREGATE.toMap());
        final String testAggregateRootJsonString = jsonObject.toJSONString();
        this.mockMvc.perform(get("/v1/myaggregate/" + TEST_ID))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(testAggregateRootJsonString));
    }

    @Test
    public void shouldReturn404ForReadWithNonexistentId() throws Exception {
        this.mockMvc.perform(get("/v1/myaggregate/nonexistentId"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldReturn404ForUpdateWithNonexistentId() throws Exception {
        //
        Mockito.doThrow(new ObjectNotFoundException("Thrown from unit test"))
                .when(service).update(anyMap());
        //
        final JSONObject jsonObject = new JSONObject(TEST_AGGREGATE.toMap());
        final String testAggregateRootJsonString = jsonObject.toJSONString();
        this.mockMvc.perform(
                put("/v1/myaggregate/" + TEST_ID)
                        .content(testAggregateRootJsonString)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200ForValidUpdate() throws Exception {
        //
        Mockito.doNothing().when(service).update(anyMap());
        //
        final JSONObject jsonObject = new JSONObject(TEST_AGGREGATE.toMap());
        final String testAggregateRootJsonString = jsonObject.toJSONString();
        this.mockMvc.perform(
                put("/v1/myaggregate/" + TEST_ID)
                        .content(testAggregateRootJsonString)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldReturn200ForDelete() throws Exception {
        //
        Mockito.doNothing().when(service).delete(anyString());
        //
        final JSONObject jsonObject = new JSONObject(TEST_AGGREGATE.toMap());
        final String testAggregateRootJsonString = jsonObject.toJSONString();
        this.mockMvc.perform(
                put("/v1/myaggregate/" + TEST_ID)
                        .content(testAggregateRootJsonString)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
