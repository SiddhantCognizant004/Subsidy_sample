//package com.cts.agrichain.subsidy.filter;
//
//import com.cts.agrichain.subsidy.service.JWTService;
//import com.cts.agrichain.subsidy.service.SecurityServiceImpl;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private SecurityServiceImpl securityService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        // No token — skip filter, Spring Security will handle as unauthenticated
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        String username = jwtService.extractUserName(token);
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = securityService.loadUserByUsername(username);
//
//            if (jwtService.validateToken(token, userDetails)) {
//
//                // ✅ Extract role from JWT token claims
//                // Your Auth service must put role in the token like: claims.put("role", "FARMER")
//                String role = jwtService.extractRole(token);
//
//                // ✅ Build the authority from the role in the token
//                // Spring Security expects "ROLE_FARMER", "ROLE_ADMIN" etc.
//                // SimpleGrantedAuthority takes the full string, so we prefix with ROLE_
//                List<SimpleGrantedAuthority> authorities = List.of(
//                        new SimpleGrantedAuthority("ROLE_" + role)
//                );
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                authorities  // ✅ Real role from JWT, not hardcoded ADMIN
//                        );
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                System.out.println("Authenticated user: " + username + " | Role: " + role);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}