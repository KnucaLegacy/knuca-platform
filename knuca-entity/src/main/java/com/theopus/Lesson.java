package com.theopus;
import com.theopus.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity(name = "Lesson")
public class Lesson {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private long id;

    @JoinColumn(name = "subject_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Subject subject;
    @Column(name = "lesson_type")
    private LessonType type;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "lesson_group",
            joinColumns =@JoinColumn(name = "lesson_id"),
            inverseJoinColumns =@JoinColumn(name = "group_id") )
    private Set<Group> groups = new HashSet<>();


    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "lesson_teacher",
            joinColumns =@JoinColumn(name = "lesson_id"),
            inverseJoinColumns =@JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(targetEntity = Circumstance.class,mappedBy = "lesson",cascade = CascadeType.ALL)
    private Set<Circumstance> circumstances = new HashSet<>();

    public Lesson(Subject subject, LessonType type, Set<Group> groups, Set<Teacher> teachers,
                  Set<Circumstance> circumstances) {
        this.subject = subject;
        this.type = type;
        this.groups = groups;
        this.teachers = teachers;
        this.circumstances = circumstances;
    }

}
