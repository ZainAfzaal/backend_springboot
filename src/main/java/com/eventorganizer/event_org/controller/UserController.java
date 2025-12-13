package com.eventorganizer.event_org.controller;


import com.eventorganizer.event_org.dto.RegisterRequest;
import com.eventorganizer.event_org.service.JWTService;
import com.eventorganizer.event_org.model.App_User;
import com.eventorganizer.event_org.dto.AuthRequest;
import com.eventorganizer.event_org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping("/register")
//    public App_User registerUser(@RequestBody App_User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        if (user.getRole() == null) {
//            user.setRole(Role.ROLE_USER); // default role
//        }
//        return userrepo.save(user);
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("UserName already exists!");
        }

        App_User user = new App_User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully! ");


    }

     @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("UserName already exists!");
        }

        App_User user = new App_User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setRole("ADMIN");
        userRepository.save(user);
        return ResponseEntity.ok("ADMIN created  successfully! ");


    }


    @PostMapping("/login")
    public String generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        }else {
            throw new UsernameNotFoundException("Invalid user!");
        }
    }


    @GetMapping("/all")
    public List<App_User> getAllUser() {
        return userRepository.findAll();
    }


    @GetMapping("/{id}")
    public App_User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Id not found"));
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Id not fount");
        }

        userRepository.deleteById(id);
        return "User Deleted Successfully";
    }

}
