package com.theopus.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.service.TeacherService;
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
public class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService mockGroupService;
    @InjectMocks
    private TeacherController teacherController;
    @Before
    public void all() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(teacherController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void byIdNotFound() throws Exception {
        when(mockGroupService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Teacher teacher = new Teacher("teacher");
        teacher.setId(1L);
        when(mockGroupService.get(1L)).thenReturn(teacher);
        MvcResult mvcResult = mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(teacher), actual);
    }

    @Test
    public void findAll() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Teacher teacher1 = new Teacher("one");
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher("two");
        teacher2.setId(2L);
        Teacher teacher3 = new Teacher("three");
        teacher2.setId(3L);
        List<Teacher> teachers = Lists.newArrayList(teacher2, teacher2, teacher3);
        when(mockGroupService.getAll()).thenReturn(teachers);

        MvcResult mvcResult = mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(teachers), actual);
    }
}