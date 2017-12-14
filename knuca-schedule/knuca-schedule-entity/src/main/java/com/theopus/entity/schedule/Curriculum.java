package com.theopus.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity(name = "curriculum")
public class Curriculum {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(targetEntity = Group.class)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Circumstance> circumstances = new HashSet<>();

    public Curriculum(Course course, Group group, Set<Circumstance> circumstances) {
        this.course = course;
        this.group = group;
        this.circumstances = circumstances;
    }
}



