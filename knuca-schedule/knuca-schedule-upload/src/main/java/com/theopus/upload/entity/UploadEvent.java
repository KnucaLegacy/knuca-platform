package com.theopus.upload.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.theopus.upload.constants.Views;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "upload_event")
public class UploadEvent {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JsonView(Views.Short.class)
    private Long id;
    @Column(name = "status")
    @JsonView(Views.Short.class)
    private Status status;
    @Column(name = "last_processing")
    @JsonView(Views.Short.class)
    private LocalDateTime lastProcessing;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "event_file",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    @JsonView(Views.Short.class)
    private List<UploadFile> files;

    public UploadEvent() {
        files = new ArrayList<>();
    }

    public enum Status {
        CREATED,
        SUBMITTED,
        IN_PROCESS,
        SUCCEEDED,
        FAILED,
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getLastProcessing() {
        return lastProcessing;
    }

    public void setLastProcessing(LocalDateTime lastProcessing) {
        this.lastProcessing = lastProcessing;
    }

    public List<UploadFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadFile> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UploadEvent event = (UploadEvent) o;
        return status == event.status &&
                Objects.equals(lastProcessing, event.lastProcessing) &&
                files.containsAll(event.files) &&
                event.files.containsAll(files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, lastProcessing, files);
    }

    @Override
    public String toString() {
        return "UploadEvent{" +
                "id=" + id +
                ", status=" + status +
                ", lastProcessing=" + lastProcessing +
                ", files=" + files +
                '}';
    }
}
