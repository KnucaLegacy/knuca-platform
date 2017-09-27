package com.theopus;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Oleksandr_Tkachov on 9/15/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@Entity(name = "Groupp")
public class Group {

    @Id@GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    private long id;
    @Column(name = "name")
    private String name;

    public Group(String name) {
        this.name = name;
    }


}
