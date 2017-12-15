package com.theopus.parser.obj;

import com.theopus.entity.schedule.enums.LessonOrder;

public class LessonOrderChain {
    private LessonOrderChain previous;
    private LessonOrder value;
    private LessonOrderChain next;

    public LessonOrderChain(LessonOrder value) {
        this.previous = previous;
        this.value = value;
        this.next = next;
    }

    public LessonOrderChain() {

    }

    public LessonOrderChain getPrevious() {
        return previous;
    }

    public void setPrevious(LessonOrderChain previous) {
        this.previous = previous;
    }

    public LessonOrder getValue() {
        return value;
    }

    public void setValue(LessonOrder value) {
        this.value = value;
    }

    public LessonOrderChain getNext() {
        return next;
    }

    public void setNext(LessonOrderChain next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "LessonOrderChain{" +
                "previous=" + previous +
                ", value=" + value +
                ", next=" + next +
                '}';
    }
}
