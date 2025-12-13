package com.eventorganizer.event_org.security;

import com.eventorganizer.event_org.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;



    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(requests -> requests
                // This code says that everyone can (without authentication) GET/access all events by this path - /events/all.
               .requestMatchers(HttpMethod.GET, "/events/all").permitAll()
               .requestMatchers("/users/register").permitAll()
                .requestMatchers("/users/register/admin").permitAll()

                // This code says USER & ADMIN role can GET/access any event by id using this path - /events/{id}. -- ** -> same like {id} or any path
                .requestMatchers(HttpMethod.GET, "/events/{id}").hasAnyRole("USER", "ADMIN")

                // This code says USER & ADMIN role can  POST new events by this path - /events/add
                .requestMatchers(HttpMethod.POST, "/events/add").hasAnyRole("USER", "ADMIN")

                // Only ADMIN role can PUT/update events using this path - /events/update/{id} -- ** -> same like id or any path
                .requestMatchers(HttpMethod.PUT, "/events/update/**").hasRole("ADMIN")

                // Only ADMIN role can Patch/update partial events using this path - /events/update-partial/{id} -- ** -> same like {id} or any path
                .requestMatchers(HttpMethod.PATCH, "/events/update-partial/**").hasRole("ADMIN")

                // Only ADMIN role can DELETE any events using this path - /events/delete/{id} -- ** -> same like {id} or any path
                .requestMatchers(HttpMethod.DELETE, "/events/delete/**").hasRole("ADMIN")

                // Only Admin will handle user CRUD Operation...


                .requestMatchers("/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/delete/{id}").hasRole("ADMIN")



                // All other requests must have to give authentication to log in.
                .anyRequest().authenticated()
        );

        //Stateful = the server remembers you after you log in.
        //Stateless = the server forgets you, so you must send your login info every time.
        //We use stateless for APIs because it’s simple, fast, and works better with tokens like JWT.
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);



        //http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
