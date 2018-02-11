package com.theopus.restservice.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Oleksandr_Tkachov on 11.02.2018.
 */
@RunWith(SpringRunner.class)
public class RestExceptionHandlerTest {

    @Mock
    private GroupController controller;
    private MockMvc mockMvc;

    @Before
    public void all() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void testAnyException() throws Exception {
        when(controller.all()).thenThrow(new RuntimeException("test exception"));
        mockMvc.perform(get("/groups"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError());
    }
}