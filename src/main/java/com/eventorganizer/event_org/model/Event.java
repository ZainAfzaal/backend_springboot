package com.eventorganizer.event_org.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    

    @Column(name = "created_by")
    private String createdBy;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column (name = "Participants")
    private List<String> joinedBy = new ArrayList<>();

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

    
}
