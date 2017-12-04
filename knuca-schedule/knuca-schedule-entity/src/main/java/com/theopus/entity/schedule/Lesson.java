package com.theopus.entity.schedule;
import com.theopus.entity.schedule.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a concept of some classes at some time at some places.
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity(name = "Lesson")
//@Table(uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"subject_id", "lesson_type", "teachers"})
//})
public class Lesson {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @Column(name = "lesson_type")
    private LessonType type;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "lesson_teacher",
            joinColumns =@JoinColumn(name = "lesson_id"),
            inverseJoinColumns =@JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Circumstance> circumstances = new HashSet<>();

    public Lesson(Subject subject, LessonType type, List<Teacher> teachers,
                  Set<Circumstance> circumstances) {
        this.subject = subject;
        this.type = type;
        this.teachers = teachers;
        this.circumstances = circumstances;
    }

}
