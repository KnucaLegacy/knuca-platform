package com.theopus.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theopus.entity.schedule.Group;
import com.theopus.entity.schedule.Room;
import com.theopus.entity.schedule.Teacher;
import com.theopus.repository.service.GroupService;
import com.theopus.repository.service.LessonService;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.TeacherService;
import com.theopus.restservice.config.JacksonConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Oleksandr_Tkachov on 11.02.2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JacksonConfig.class)
public class LessonControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private JacksonConfig jacksonConfig;

    @InjectMocks
    private LessonController lessonController;
    @Mock
    private LessonService lessonService;
    @Mock
    private GroupService groupService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private RoomService roomService;

    @Before
    public void all() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(jacksonConfig.objectMapper());
        FormattingConversionService formattingConversionService = new FormattingConversionService();
        mockMvc = MockMvcBuilders
                .standaloneSetup(lessonController)
                .setControllerAdvice(new RestExceptionHandler())
                .setMessageConverters(converter)
                .build();
    }

    @Test
    public void byGroupEntityNotFound() throws Exception {
        when(groupService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/lessons/group/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byTeacherEntityNotFound() throws Exception {
        when(teacherService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/lessons/teacher/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byRoomEntityNotFound() throws Exception {
        when(roomService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/lessons/room/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byGroup() throws Exception {
        Group group = new Group("TestGroup");
        group.setId(1L);
        when(groupService.get(1L)).thenReturn(group);
        mockMvc.perform(get("/lessons/group/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    @Ignore
    public void byGroupAndDate() throws Exception {
        Group group = new Group("TestGroup");
        group.setId(1L);
        when(groupService.get(1L)).thenReturn(group);
        mockMvc.perform(get("/lessons/2018-02-09/group/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byGroupAndWeek() throws Exception {
        Group group = new Group("TestGroup");
        group.setId(1L);
        when(groupService.get(1L)).thenReturn(group);
        mockMvc.perform(get("/lessons/week/group/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byTeacher() throws Exception {
        Teacher teacher = new Teacher("TestTeacher");
        teacher.setId(1L);
        when(teacherService.get(1L)).thenReturn(teacher);
        mockMvc.perform(get("/lessons/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    @Ignore
    public void byTeacherAndDate() throws Exception {
        Teacher teacher = new Teacher("TestTeacher");
        teacher.setId(1L);
        when(teacherService.get(1L)).thenReturn(teacher);
        mockMvc.perform(get("/lessons/2018-02-09/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byTeacherAndWeek() throws Exception {
        Teacher teacher = new Teacher("TestTeacher");
        teacher.setId(1L);
        when(teacherService.get(1L)).thenReturn(teacher);
        mockMvc.perform(get("/lessons/week/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byRoom() throws Exception {
        Room room = new Room("TestRoom");
        room.setId(1L);
        when(roomService.get(1L)).thenReturn(room);
        mockMvc.perform(get("/lessons/room/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    @Ignore
    public void byRoomAndDate() throws Exception {
        Room room = new Room("TestRoom");
        room.setId(1L);
//        when(roomService.get(1L)).thenReturn(room);
        MvcResult mvcResult = mockMvc.perform(get("/lessons/2018-02-09/room/1"))
                .andReturn();

        System.out.println(mvcResult.getResponse().getStatus());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void byRoomAndWeek() throws Exception {
        Room room = new Room("TestRoom");
        room.setId(1L);
        when(roomService.get(1L)).thenReturn(room);
        mockMvc.perform(get("/lessons/week/room/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

}