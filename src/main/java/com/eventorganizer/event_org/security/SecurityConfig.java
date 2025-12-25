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

        http
               
               .authorizeHttpRequests(requests -> requests
                
               .requestMatchers(HttpMethod.GET, "/events/all").permitAll()
               .requestMatchers("/users/register").permitAll()
                .requestMatchers("/users/register/admin").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                  

               
                .requestMatchers(HttpMethod.GET, "/events/{id}").hasAnyRole("USER", "ADMIN")

                
                .requestMatchers(HttpMethod.POST, "/events/add").hasAnyRole("USER", "ADMIN")

               
                .requestMatchers(HttpMethod.PUT, "/events/update/**").hasRole("ADMIN")

               
                .requestMatchers(HttpMethod.PATCH, "/events/update-partial/**").hasRole("ADMIN")

                
                .requestMatchers(HttpMethod.DELETE, "/events/delete/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/events/join/**").hasAnyRole("USER", "ADMIN")

                


                .requestMatchers("/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/delete/{id}").hasRole("ADMIN")



                
                .anyRequest().authenticated()
        );

        http.cors(Customizer.withDefaults());
        

       
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);



        //http.formLogin(Customizer.withDefaults());
        //http.httpBasic(Customizer.withDefaults());

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
