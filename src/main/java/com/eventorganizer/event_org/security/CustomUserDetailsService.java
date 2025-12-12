package com.eventorganizer.event_org.security;

import com.eventorganizer.event_org.model.App_User;
import com.eventorganizer.event_org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        App_User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //String roleWithPrefix = "ROLE_" + user.getRole();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                //.roles(roleWithPrefix.replace("Role_", ""))
                .roles(user.getRole())
                .build();
    }
}
