package com.theopus.entity.schedule;

import com.google.common.collect.Sets;
import com.theopus.entity.schedule.enums.LessonOrder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/18/2017.
 */

@Entity(name = "circumstance")
public class Circumstance {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "lesson_order")
    private LessonOrder lessonOrder;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(cascade = {
            CascadeType.DETACH})
    private Curriculum curriculum;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dates", joinColumns = @JoinColumn(name = "circumstance_id"))
    @Column(name = "date")
    private Set<LocalDate> dates = new HashSet<>();

    public Circumstance() {
    }

    public Circumstance(LessonOrder lessonOrder, Room room, Set<LocalDate> dates) {
        this.lessonOrder = lessonOrder;
        this.room = room;
        this.dates = dates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circumstance that = (Circumstance) o;
        return lessonOrder == that.lessonOrder &&
                Objects.equals(room, that.room) &&
                Objects.equals(curriculum, that.curriculum) &&
                Sets.difference(dates, that.dates).isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonOrder, room, dates);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LessonOrder getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(LessonOrder lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public Set<LocalDate> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDate> dates) {
        this.dates = dates;
    }


    @Override
    public String toString() {
        return "Circumstance{" +
                "id=" + id +
                ", lessonOrder=" + lessonOrder +
                ", room=" + room +
                ", dates=" + dates +
                '}';
    }
}
