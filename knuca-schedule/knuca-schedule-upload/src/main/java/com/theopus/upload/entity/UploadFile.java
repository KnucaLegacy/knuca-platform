package com.theopus.upload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.upload.constants.FileExtension;
import com.theopus.upload.constants.Views;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "upload_file")
public class UploadFile {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JsonView(Views.Short.class)
    private Long id;

    @JsonView(Views.Short.class)
    @Column(name = "name")
    private String name;

    @Column(name = "content", columnDefinition = "longtext")
    private String content;

    @Column(name = "total_sheets")
    @JsonView(Views.Short.class)
    private Integer totalSheets;

    @Column(name = "assigned_year")
    @JsonView(Views.Short.class)
    private Integer assignedYear;

    @JsonView(Views.Short.class)
    @Column(name = "extension")
    private FileExtension extension;

    @JsonView(Views.Short.class)
    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @JsonIgnore
    @ManyToMany(mappedBy = "files", fetch = FetchType.EAGER)
    private List<UploadEvent> events;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTotalSheets() {
        return totalSheets;
    }

    public void setTotalSheets(Integer totalSheets) {
        this.totalSheets = totalSheets;
    }

    public FileExtension getExtension() {
        return extension;
    }

    public void setExtension(FileExtension extension) {
        this.extension = extension;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public List<UploadEvent> getEvents() {
        return events;
    }

    public void setEvents(List<UploadEvent> events) {
        this.events = events;
    }

    public Integer getAssignedYear() {
        return assignedYear;
    }

    public void setAssignedYear(Integer assignedYear) {
        this.assignedYear = assignedYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadFile that = (UploadFile) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(content, that.content) &&
                Objects.equals(totalSheets, that.totalSheets) &&
                Objects.equals(assignedYear, that.assignedYear) &&
                extension == that.extension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, totalSheets, assignedYear, extension, uploadTime);
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", totalSheets=" + totalSheets +
                ", assignedYear=" + assignedYear +
                ", extension=" + extension +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
