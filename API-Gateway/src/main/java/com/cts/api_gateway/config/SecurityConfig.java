package com.cts.api_gateway.config;

import com.cts.api_gateway.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtFilter jwtFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Public Endpoints
                        .pathMatchers("/users/login", "/users/register", "/actuator/**").permitAll()
                        .pathMatchers("/market/test-proxy").permitAll()

                        // Role-based Endpoints
                        .pathMatchers("/api/report/**").hasRole("ADMIN")
                        .pathMatchers("/api/transactions/**").hasAnyRole("TRADER", "FARMER", "ADMIN")

                        // 2. Crop Listing Permissions (Migrated from old config)
                        .pathMatchers(HttpMethod.POST, "/market/createlisting").hasRole("FARMER")
                        .pathMatchers(HttpMethod.PUT, "/market/listings/validate/**").hasRole("OFFICER")
                        // GET for listings - Accessible by all roles
                        .pathMatchers(HttpMethod.GET, "/market/listings/**").hasAnyRole("FARMER", "TRADER", "OFFICER", "ADMIN")

                        // 3. Order Permissions (Migrated from old config)
                        .pathMatchers(HttpMethod.POST, "/market/placeorder").hasRole("TRADER")
                        .pathMatchers("/market/orders/**").hasAnyRole("TRADER", "OFFICER", "ADMIN")
                        // Farmer Management
                        .pathMatchers("/farmers/**").hasAnyRole("ADMIN", "OFFICER")

                        // --- 2. Subsidy Program (Integrated) ---
                        .pathMatchers(HttpMethod.POST, "/subsidy-programs/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/subsidy-programs/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/subsidy-programs/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/subsidy-programs/**").authenticated()

                        // --- 3. Disbursement (Integrated) ---
                        .pathMatchers(HttpMethod.POST, "/disbursements/**").hasRole("FARMER")
                        .pathMatchers(HttpMethod.PUT, "/disbursements/**").hasAnyRole("ADMIN", "OFFICER")
                        .pathMatchers(HttpMethod.PATCH, "/disbursements/*/review").hasAnyRole("ADMIN", "OFFICER")
                        .pathMatchers(HttpMethod.GET, "/disbursements/**").authenticated()

                        .pathMatchers("/transactions/**").hasAnyRole("TRADER", "FARMER", "ADMIN")
                        .pathMatchers("/payments/**").hasAnyRole("TRADER", "ADMIN")

                        // --- 2. Audit & Compliance (Newly Added) ---
                        .pathMatchers("/api/audits/**").hasAnyRole("AUDITOR", "ADMIN")
                        .pathMatchers("/api/compliances/**").hasAnyRole("COMPLIANCE", "ADMIN")

                        // AuditLog: Any authenticated user can POST (log), only ADMIN & AUDITOR can GET (view)
                        .pathMatchers(HttpMethod.POST, "/api/auditlogs/**").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/auditlogs/**").hasAnyRole("ADMIN", "AUDITOR")

                        .anyExchange().authenticated()
                )
                // Add your custom filter
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}