package com.spinozanose.springbootrestoo.myAggregate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MyAggregatesController.class)
public class MyAggregatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyAggregateService service;

    /**
     * The search service has not yet been implemented, but we can still test the
     * controller.
     */
    @Test
    public void shouldReturnEmptyListWithNoParameters() throws Exception {
        //
        Map<String, String> parameters = null;
        when(service.search(parameters)).thenReturn(null);
        //
        this.mockMvc.perform(get("/v1/myaggregates"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
