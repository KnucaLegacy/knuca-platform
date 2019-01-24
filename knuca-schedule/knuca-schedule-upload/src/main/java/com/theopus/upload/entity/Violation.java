package com.theopus.upload.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.upload.constants.Views;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "violation")
public class Violation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JsonView(Views.Short.class)
    @Column(name = "violation_id")
    private Long id;

    @ManyToOne
    private Report report;

    @Column(name = "violation_date")
    @JsonView(Views.Short.class)
    private LocalDate violationDate;

    @ElementCollection()
    @CollectionTable(
            name = "violation_expected",
            joinColumns = @JoinColumn(name = "violation_id")
    )
    @JsonView(Views.Short.class)
    private List<ViolationEntry> expected;

    @ElementCollection
    @CollectionTable(
            name = "violation_actual",
            joinColumns = @JoinColumn(name = "violation_id")
    )
    @JsonView(Views.Short.class)
    private List<ViolationEntry> actual;

    public Violation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public LocalDate getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(LocalDate violationDate) {
        this.violationDate = violationDate;
    }

    public List<ViolationEntry> getExpected() {
        return expected;
    }

    public void setExpected(List<ViolationEntry> expected) {
        this.expected = expected;
    }

    public List<ViolationEntry> getActual() {
        return actual;
    }

    public void setActual(List<ViolationEntry> actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "Violation{" +
                "id=" + id +
                ", violationDate=" + violationDate +
                ", expected=" + expected +
                ", actual=" + actual +
                '}';
    }
}
