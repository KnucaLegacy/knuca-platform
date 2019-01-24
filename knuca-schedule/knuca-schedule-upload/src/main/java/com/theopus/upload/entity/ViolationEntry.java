package com.theopus.upload.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.entity.schedule.enums.LessonOrder;
import com.theopus.entity.schedule.enums.LessonType;
import com.theopus.upload.constants.Views;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ViolationEntry implements Comparable<ViolationEntry> {


    @JsonView(Views.Short.class)
    @Column(name = "lesson_order")
    private LessonOrder lessonOrder;
    @Column(name = " lesson_type")
    @JsonView(Views.Short.class)
    private LessonType lessonType;

    public ViolationEntry() {
    }

    public LessonOrder getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(LessonOrder lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViolationEntry that = (ViolationEntry) o;
        return lessonOrder == that.lessonOrder &&
                lessonType == that.lessonType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonOrder, lessonType);
    }

    @Override
    public String toString() {
        return "ViolationEntry{" +
                "lessonOrder=" + lessonOrder +
                ", lessonType=" + lessonType +
                '}';
    }

    @Override
    public int compareTo(ViolationEntry o) {
        return Integer.compare(this.lessonOrder.ordinal(), o.lessonOrder.ordinal());
    }
}
