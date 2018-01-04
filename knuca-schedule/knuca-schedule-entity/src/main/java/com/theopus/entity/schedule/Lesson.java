package com.theopus.entity.schedule;

import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for future.
 */
public class Lesson {

    private Subject subject;
    private LessonOrder order;
    private LessonType type;
    private List<Room> roomList;
    private List<Teacher> teachers;
    private List<Group> groups;
    private LocalDate localDate;

    public Lesson() {
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LessonOrder getOrder() {
        return order;
    }

    public void setOrder(LessonOrder order) {
        this.order = order;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "subject=" + subject +
                ", order=" + order +
                ", type=" + type +
                ", roomList=" + roomList +
                ", teachers=" + teachers +
                ", groups=" + groups +
                ", localDate=" + localDate +
                '}';
    }
}
