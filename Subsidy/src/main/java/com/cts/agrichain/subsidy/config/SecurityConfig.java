//package com.cts.agrichain.subsidy.config;
//
//import com.cts.agrichain.subsidy.filter.JwtFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//
//                        .requestMatchers("/actuator/**").permitAll()
//
//                        // Subsidy Program
//                        .requestMatchers(HttpMethod.POST, "/subsidy-programs/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/subsidy-programs/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/subsidy-programs/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/subsidy-programs/**").authenticated()
//
//                        // Disbursement
//                        .requestMatchers(HttpMethod.POST, "/disbursements/**").hasRole("FARMER")
//                        .requestMatchers(HttpMethod.GET, "/disbursements/**").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/disbursements/**").hasAnyRole("ADMIN", "OFFICER")
//                        .requestMatchers(HttpMethod.PATCH, "/disbursements/*/review").hasAnyRole("ADMIN", "OFFICER")
//
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}