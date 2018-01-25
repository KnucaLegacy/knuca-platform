package com.theopus.entity.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */

@Entity(name = "room")
public class Room {

    public static final String NO_AUDITORY = "NO_AUDITORY";

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Column(unique = true, name = "name")
    private String name;

    @OneToMany(mappedBy = "room", cascade = {CascadeType.REMOVE, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Circumstance> circumstances = new HashSet<>();

    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNoAuditory() {
        return NO_AUDITORY;
    }

    public Set<Circumstance> getCircumstances() {
        return circumstances;
    }

    public void setCircumstances(Set<Circumstance> circumstances) {
        this.circumstances = circumstances;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
