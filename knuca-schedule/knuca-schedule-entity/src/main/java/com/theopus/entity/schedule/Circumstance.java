package com.theopus.entity.schedule;

import com.theopus.entity.schedule.enums.LessonOrder;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/18/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "curriculum"})
@Entity(name = "circumstance")
public class Circumstance {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "lesson_order")
    private LessonOrder lessonOrder;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(targetEntity = Curriculum.class)
    @JoinColumn(name = "course_id")
    private Curriculum curriculum;

    @ElementCollection
    @CollectionTable(name = "dates", joinColumns = @JoinColumn(name = "circumstance_id"))
    @Column(name = "date")
    private Set<LocalDate> dates = new HashSet<>();

    public Circumstance(LessonOrder lessonOrder, Room room, Set<LocalDate> dates) {
        this.lessonOrder = lessonOrder;
        this.room = room;
        this.dates = dates;
    }
}
