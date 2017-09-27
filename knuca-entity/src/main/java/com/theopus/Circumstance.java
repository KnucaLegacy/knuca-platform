package com.theopus;

import com.theopus.enums.LessonOrder;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/18/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "lesson")
@EqualsAndHashCode(exclude = {"id", "lesson"})
@Entity(name = "Circumstance")
public class Circumstance {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name = "lesson_order")
    private LessonOrder lessonOrder;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "circumstance_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "circumstance_id"))
    private Set<Room> rooms = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "dates", joinColumns = @JoinColumn(name = "circumstance_id"))
    @Column(name = "date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private Set<LocalDate> dates = new HashSet<>();

    @ManyToOne(targetEntity = Lesson.class)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Circumstance(LessonOrder lessonOrder, Set<Room> rooms, Set<LocalDate> dates, Lesson lesson) {
        this.lessonOrder = lessonOrder;
        this.rooms = rooms;
        this.dates = dates;
        this.lesson = lesson;
    }
}
