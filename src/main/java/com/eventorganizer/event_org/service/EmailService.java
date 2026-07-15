package com.eventorganizer.event_org.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sednregistrationemail(String toEmail, String userName ){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Welcome to EventHub");
        mail.setText("Hi " + userName + ",\n\n" +
                "Welcome aboard!\n\n" +
                "Your account is now live, and you are all set to start exploring, creating, and managing amazing events.\n\n" +
                "Whether you're planning something big or just browsing what's happening, we’ve got everything you need in one place.\n\n" +
                "Jump in and get started now!\n\n" +
                "If you ever need help, we’re just a message away.\n\n" +
                "Cheers,\n" +
                "The EventHub Team"

        );

        javaMailSender.send(mail);
    }

    public void sendNewEventMail(String toEmail, String username){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Event Created Successfully");
        mail.setText("Hello " + username + ",\n\n" +
                "Your event has been created successfully.\n\n" +
                "You can view or manage it from your dashboard.\n\n" +
                "Best regards,\n" +
                "Event Management Team");

        javaMailSender.send(mail);
    }

    public void sendCancellationEmail(String toEmail, String eventTitle){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Event Cancelled: " + eventTitle);
        mail.setText("Hi,\n\n" +
                            "We're sorry to inform you that the event " + eventTitle +  " has been cancelled.\n\n" +
                            "We hope to see you at future events!\n\n"  +
                            "Cheers,\n" +
                            "The EventHub Team");
        javaMailSender.send(mail);
    }

    public void sendUpdateEmail(String toEmail, String eventTitle){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Event " + eventTitle + " is Updated.");
        mail.setText( "Hello,\n\n" +
                "The event " + eventTitle + " has been updated.\n\n" +
                "Please check the app for the latest details.\n\n" +
                "Best regards,\n" +
                "Event Management Team");
        javaMailSender.send(mail);
    }

    public void adminAnnouncement(String toEmail, String subject, String body ){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("[Announcement] " + subject);
        mail.setText("Hi,\n\n" + body + "\n\nRegards,\nThe EventHub Admin Team");
        javaMailSender.send(mail);
    }

    public void adminCancellationEvent(String toEmail, String eventTitle){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Event Cancelled by Admin: " + eventTitle);
        mail.setText("Hello,\n\n" +
                "We regret to inform you that the event " + eventTitle + "  has been cancelled by the administrator.\n\n" +
                "We apologize for any inconvenience this may cause.\n\n" +
                "For more information, please contact support.\n\n" +
                "Best regards,\n" +
                "Event Management Team");
        javaMailSender.send(mail);
    }

    public void adminUpdateEvent(String toEmail, String eventTitle){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject("Event Updated by Admin: " + eventTitle);
        mail.setText("Hello,\n\n" +
                "The event " + eventTitle + " has been updated by the administrator.\n\n" +
                "Please review the latest changes such as date, location, or description in the system.\n\n" +
                "Best regards,\n" +
                "Event Management Team");
        javaMailSender.send(mail);
    }



}
