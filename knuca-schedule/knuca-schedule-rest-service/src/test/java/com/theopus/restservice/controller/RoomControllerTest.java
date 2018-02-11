package com.theopus.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.theopus.entity.schedule.Room;
import com.theopus.repository.service.RoomService;
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
public class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService mockRoomService;
    @InjectMocks
    private RoomController roomController;
    @Before
    public void all() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(roomController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void byIdNotFound() throws Exception {
        when(mockRoomService.get(1L)).thenReturn(null);
        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
    }

    @Test
    public void byId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Room room = new Room("Room");
        room.setId(1L);
        when(mockRoomService.get(1L)).thenReturn(room);
        MvcResult mvcResult = mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(room), actual);
    }

    @Test
    public void findAll() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Room room1 = new Room("one");
        room1.setId(1L);
        Room room2 = new Room("two");
        room2.setId(2L);
        Room room3 = new Room("three");
        room1.setId(3L);
        List<Room> rooms = Lists.newArrayList(room1, room1, room3);
        when(mockRoomService.getAll()).thenReturn(rooms);

        MvcResult mvcResult = mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(rooms), actual);
    }
}