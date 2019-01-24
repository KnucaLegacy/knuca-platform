package com.theopus.upload.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.upload.constants.Views;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Entity(name = "report")
public class Report {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JsonView(Views.Short.class)

    private Long id;

    @ManyToOne
    @JsonView(Views.Short.class)
    private UploadFile file;

    @OneToMany(mappedBy = "report", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonView(Views.Short.class)
    private Set<Violation> errors;

    @ElementCollection
    @MapKeyColumn(name = "date")
    @JsonView(Views.Short.class)
    @Column(name = "parsed_value") // column name for map "value"
    private Map<LocalDate, String> daysMap;


    @Column(name = "content", columnDefinition = "longtext")
    private String content;
    @Column(name = "anchor")
    @JsonView(Views.Short.class)
    private String anchor;
    @Column(name = "sheet_number")
    @JsonView(Views.Short.class)
    private Integer sheetNumber;

    @Column(name = "report_date")
    @JsonView(Views.Short.class)
    private LocalDate reportDate;


    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UploadFile getFile() {
        return file;
    }

    public void setFile(UploadFile file) {
        this.file = file;
    }


    public Set<Violation> getErrors() {
        return errors;
    }

    public void setErrors(Set<Violation> errors) {
        this.errors = errors;
    }

    public Map<LocalDate, String> getDaysMap() {
        return daysMap;
    }

    public void setDaysMap(Map<LocalDate, String> daysMap) {
        this.daysMap = daysMap;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Integer getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(Integer sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
