package com.theopus.entity.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity(name = "group_")
public class Group {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private Long id;
    @Column(unique = true, name = "name")
    private String name;

    public Group(String name) {
        this.name = name;
    }


}
