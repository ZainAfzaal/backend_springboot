package com.eventorganizer.event_org.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private  String title;
    private String date;
    private String location;
    private  String description;

    @Version
    private Long version;

    @Column(name = "created_by")
    private String createdBy;

    @ElementCollection
    @Column (name = "Participants")
    private List<String> joinedBy = new ArrayList<>();

    private Long maxParticipants;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public List<String> getJoinedBy() {
        return joinedBy;
    }

    public void setJoinedBy(List<String> joinedBy) {
        this.joinedBy = joinedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }



}
