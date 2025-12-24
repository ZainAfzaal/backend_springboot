package com.eventorganizer.event_org.controller;

import com.eventorganizer.event_org.model.Event;
import com.eventorganizer.event_org.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService service;

    // @PostMapping("/add")
    // public Event addEvent(@RequestBody Event event) {
    //     return service.addevent(event);
    // }

     @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {

        String username  = SecurityContextHolder.getContext().getAuthentication().getName();

        App_User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        event.setCreatedBy(user.getUsername());

        return eventRepository.save(event);
    }

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return service.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        return service.deleteEvent(id);
    }
}
