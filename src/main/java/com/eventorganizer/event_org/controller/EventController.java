package com.eventorganizer.event_org.controller;

import com.eventorganizer.event_org.model.Event;
import com.eventorganizer.event_org.model.App_User;
import com.eventorganizer.event_org.repository.EventRepository;
import com.eventorganizer.event_org.repository.UserRepository;
import com.eventorganizer.event_org.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/join/{id}")
    public Event joinEvent(@PathVariable Long id) {

        String username  = SecurityContextHolder.getContext().getAuthentication().getName();
        App_User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Event not found"));

        if(event.getJoinedBy()== null){
            event.setJoinedBy(new ArrayList<>());
        }

        if (!event.getJoinedBy().contains(user.getUsername())){
            event.getJoinedBy().add(user.getUsername());
            eventRepository.save(event);

        }

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

    // @DeleteMapping("/delete/{id}")
    // public String deleteEvent(@PathVariable Long id) {
    //     return service.deleteEvent(id);
    // }

    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("event not found"));

        if(role.equals("ROLE_ADMIN")){
            eventRepository.delete(event);
            return "Event deleted Successfully by Admin";
        }

        if(!event.getCreatedBy().equals(username)){
            throw new IllegalArgumentException("You are not allowed to delete this event");
        }


        eventRepository.delete(event);
        return "Event deleted.";
    }
}
