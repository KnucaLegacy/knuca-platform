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
@Entity(name = "course")
public class Course {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @Column(name = "lesson_type")
    private LessonType type;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "course_teacher",
            joinColumns =@JoinColumn(name = "course_id"),
            inverseJoinColumns =@JoinColumn(name = "teacher_id"))
    private List<Teacher> teachers = new ArrayList<>();

    public Course(Subject subject, LessonType type, List<Teacher> teachers,
                  Set<Curriculum> сurriculums) {
        this.subject = subject;
        this.type = type;
        this.teachers = teachers;
    }

}
