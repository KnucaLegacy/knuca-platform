package com.theopus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */

@Data
@EqualsAndHashCode(exclude = {"id", })
//@ToString(exclude = "lessons")
@Entity(name = "Teacher")
public class Teacher {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private long id;
    @Column(name = "name")
    private String name;

}
