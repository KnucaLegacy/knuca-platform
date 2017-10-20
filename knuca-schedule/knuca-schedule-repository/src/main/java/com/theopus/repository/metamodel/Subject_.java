package com.theopus.repository.metamodel;

import com.theopus.entity.schedule.Subject;
import org.hibernate.annotations.Generated;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Subject.class )
public class Subject_ {
    public static volatile SingularAttribute<Subject, Long> id;
    public static volatile SingularAttribute<Subject, String> name;
}
