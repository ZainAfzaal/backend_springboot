package com.eventorganizer.event_org.service;


import com.eventorganizer.event_org.model.Event;
import com.eventorganizer.event_org.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    public EventRepository repo;

    public Event addevent(Event event){
        return repo.save(event);
    }

    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    public Event getEventById(Long id)
    {

        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Event with ID " + id + " not found"));
    }

    public Event updateEvent(Long id, Event newEvent) {

        Event existing = repo.findById(id).orElse(null);

        if (existing != null && id > 0) {
            existing.setTitle(newEvent.getTitle());
            existing.setLocation(newEvent.getLocation());
            existing.setDate(newEvent.getDate());
            existing.setDescription(newEvent.getDescription());
            return repo.save(existing);
        }

       throw new IllegalArgumentException("Invalid ID or event not found. Update failed.");

    }

    public String deleteEvent(Long id) {

        if(!repo.existsById(id)){
            throw new IllegalArgumentException("Event with ID " + id + " not found");
        }

        repo.deleteById(id);
        return "Event deleted successfully.";
    }

}
