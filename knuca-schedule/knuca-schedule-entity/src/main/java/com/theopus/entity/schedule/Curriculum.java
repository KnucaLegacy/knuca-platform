package com.theopus.entity.schedule;

import com.google.common.collect.Sets;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    public Curriculum() {
    }

    public Curriculum(Course course, Group group, Set<Circumstance> circumstances) {
        this.course = course;
        this.group = group;
        this.circumstances = circumstances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curriculum that = (Curriculum) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(course, that.course) &&
                Objects.equals(group, that.group) &&
                Sets.difference(circumstances, that.circumstances).isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, group, circumstances);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Circumstance> getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(Set<Circumstance> circumstances) {
        this.circumstances = circumstances;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "id=" + id +
                ", course=" + course +
                ", group=" + group +
                ", circumstances=" + circumstances +
                '}';
    }
}



