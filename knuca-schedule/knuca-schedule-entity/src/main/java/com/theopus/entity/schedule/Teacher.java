package com.theopus.entity.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */

@Data
@EqualsAndHashCode(exclude = {"id", })
@Entity(name = "teacher")
public class Teacher {
    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private Long id;
    @Column(unique = true, name = "name")
    private String name;

}
