package com.example.api.restapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfiguration {

    @Bean
    fun users(): UserDetailsService {
        val guest = User.builder()
            .username("guest")
            //.password("guestPass")
            .password(passwordEncoder().encode("guestPass"))
            .roles("guest")
            .build()
        val admin = User.builder()
            .username("admin")
            //.password("adminPass")
            .password(passwordEncoder().encode("adminPass"))
            .roles("admin")
            .build()
        return InMemoryUserDetailsManager(guest, admin)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.
            authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/person/**").hasRole("admin")
                    .requestMatchers("/filteredPersons").hasAnyRole("guest", "admin")
                    .anyRequest().permitAll()
            }
            .httpBasic {  }
        return http.build()
    }



}

/*
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/**").permitAll() // Allow all requests temporarily
                    // .requestMatchers("/person").hasRole("admin") // Comment out the specific rule
                    //.requestMatchers("/person").hasRole("admin")
                    //.requestMatchers("/user/**").hasRole("guest")
                    //.requestMatchers("/bothRoles/**").hasAnyRole("admin", "user") //Accessible by both roles
                    .anyRequest().authenticated()
            }
            .httpBasic { }
        return http.build()
    }

*/*/*/
*/
