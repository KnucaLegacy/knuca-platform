package com.theopus.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Group;
import com.theopus.repository.service.GroupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService mockGroupService;
    @InjectMocks
    private GroupController groupController;
    @Before
    public void all() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(groupController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void byIdNotFound() throws Exception {
        when(mockGroupService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Group group = new Group("Group");
        group.setId(1L);
        when(mockGroupService.get(1L)).thenReturn(group);
        MvcResult mvcResult = mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(group), actual);
    }

    @Test
    public void findAll() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Group group1 = new Group("one");
        group1.setId(1L);
        Group group2 = new Group("two");
        group2.setId(2L);
        Group group3 = new Group("three");
        group1.setId(3L);
        List<Group> groups = Lists.newArrayList(group1, group1, group3);
        when(mockGroupService.getAll()).thenReturn(groups);

        MvcResult mvcResult = mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(groups), actual);
    }
}