package com.eventorganizer.event_org.controller;

import com.eventorganizer.event_org.model.App_User;
import com.eventorganizer.event_org.model.Event;
import com.eventorganizer.event_org.repository.EventRepository;
import com.eventorganizer.event_org.repository.UserRepository;
import com.eventorganizer.event_org.service.EmailService;
import com.eventorganizer.event_org.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @Transactional
    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        App_User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        event.setCreatedBy(user.getUsername());


            Event savesdEvent = eventRepository.save(event);
            emailService.sendNewEventMail(user.getEmail(), user.getUsername());
            return savesdEvent;

    }

    @GetMapping("/search")
    public List<Event> searchEvents(@RequestParam String keyword){
        return eventRepository
                .searchEvents(keyword);
    }

    @GetMapping("/admin/search")
    public List<Event> searchEventsByAdmin(@RequestParam String keyword){
        return eventRepository
                .searchEventsByAdmin(keyword);
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<?> joinEvent(@PathVariable Long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            App_User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found"));

            if (event.getMaxParticipants() != null &&
                    event.getJoinedBy().size() >= event.getMaxParticipants()) {
                return ResponseEntity.status(409).body("Event is full.");
            }

            if (event.getJoinedBy().contains(user.getUsername())) {
                return ResponseEntity.status(409).body("You already joined this event.");
            }

            event.getJoinedBy().add(user.getUsername());
            eventRepository.save(event);

            return ResponseEntity.ok(event);

        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(409).body("Someone else just joined at the same time. Please try again.");
        }
    }


//    @PostMapping("/join/{id}")
//    public Event joinEvent(@PathVariable Long id) {
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        App_User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        Event event = eventRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
//
//        if (event.getMaxParticipants() != null &&
//                event.getJoinedBy().size() >= event.getMaxParticipants()) {
//            throw new IllegalArgumentException("Place is full");
//        }
//
//
//        if (event.getJoinedBy().contains(user.getUsername())) {
//            throw new IllegalArgumentException("User already join this event.");
//
//        }
//
//        event.getJoinedBy().add(user.getUsername());
//
//
//        return eventRepository.save(event);
//       }



    @Transactional
    @DeleteMapping("/leave/{id}")
    public ResponseEntity<String> leaveEvent(@PathVariable Long id) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (event.getJoinedBy() == null || !event.getJoinedBy().contains(username)) {
            throw new IllegalArgumentException(
                    "You did not join this event, so you cannot leave it ! ")
                    ;
        }
        event.getJoinedBy().remove(username);
        eventRepository.save(event);

        return ResponseEntity.ok("You have successfully leave the event!!");

    }


    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }


    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + id + " not found"));
    }

    // UPDATE (PUT)
    @Transactional
    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event newEvent) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();


        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + id + " not found"));

        existing.setTitle(newEvent.getTitle());
        existing.setLocation(newEvent.getLocation());
        existing.setDate(newEvent.getDate());
        existing.setDescription(newEvent.getDescription());

        eventRepository.save(existing);

        if (role.equals("ROLE_ADMIN")) {

            for (String participants : existing.getJoinedBy()) {
                userRepository.findByUsername(participants).ifPresent(user -> {
                    emailService.adminUpdateEvent(user.getEmail(), existing.getTitle());
                });
            }
        } else {
            for (String participants : existing.getJoinedBy()) {
                userRepository.findByUsername(participants).ifPresent(user -> {
                    emailService.sendUpdateEmail(user.getEmail(), existing.getTitle());
                });
            }
        }

        return existing;
    }

//    // PARTIAL UPDATE (PATCH)-> IT ONLY WORKS IN LOCAL HOST NOT ON "CLOUD PLATFORM"
//    @PatchMapping("/update-partial/{id}")
//    public Event updatePartial(@PathVariable Long id, @RequestBody Event eventData) {
//        Event existing = eventRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
//
//        if (eventData.getTitle() != null)
//            existing.setTitle(eventData.getTitle());
//
//        if (eventData.getDate() != null)
//            existing.setDate(eventData.getDate());
//
//        if (eventData.getLocation() != null)
//            existing.setLocation(eventData.getLocation());
//
//        if (eventData.getDescription() != null)
//            existing.setDescription(eventData.getDescription());
//
//        return eventRepository.save(existing);
//    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (role.equals("ROLE_ADMIN")) {
            // Admin can delete ANY event, no ownership check
            eventRepository.delete(event);
            for (String participant : event.getJoinedBy()) {
                userRepository.findByUsername(participant).ifPresent(user ->
                        emailService.adminCancellationEvent(user.getEmail(), event.getTitle())
                );
            }
            return "Event deleted by Admin.";

        } else {
            // Regular user can only delete their OWN event
            if (!event.getCreatedBy().equals(username)) {
                throw new IllegalArgumentException("You are not allowed to delete this event");
            }
            eventRepository.delete(event);
            for (String participant : event.getJoinedBy()) {
                userRepository.findByUsername(participant).ifPresent(user ->
                        emailService.sendCancellationEmail(user.getEmail(), event.getTitle())
                );
            }
            return "Event deleted.";
        }
    }
}
