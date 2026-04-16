//package com.cts.agrichain.subsidy.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * WHY THIS WAS BROKEN BEFORE:
// * The old code was doing .roles("ADMIN") for every single user.
// * So even if a FARMER logged in, they were treated as ADMIN here.
// * Your @PreAuthorize("hasRole('FARMER')") checks would never work correctly.
// *
// * WHY THIS WORKS NOW:
// * In a microservice setup, we don't have a UserRepo here.
// * The JWT token itself already contains the role (set when user logged in
// * via your Auth/User service). So we extract the role FROM the token.
// *
// * The JwtFilter calls loadUserByUsername(username) — but at that point
// * we don't have the token anymore. So the fix is: extract role in JwtFilter
// * itself and pass it as an authority directly. See updated JwtFilter.java.
// */
//@Service
//public class SecurityServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private JWTService jwtService;
//
//    /**
//     * This method is called by JwtFilter with the username from the token.
//     * We return a minimal UserDetails — password is blank (not needed since
//     * we're already past the authentication stage — JWT is already validated).
//     *
//     * NOTE: Role is NOT set here anymore. It is set directly in JwtFilter
//     * by extracting from the JWT claims. See JwtFilter.java.
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // We just build a shell UserDetails with the username.
//        // The actual role/authorities are injected by JwtFilter after this call.
//        return User.builder()
//                .username(username)
//                .password("") // no password check needed — JWT already validated
//                .authorities(List.of()) // empty — JwtFilter sets the real authorities
//                .build();
//    }
//}