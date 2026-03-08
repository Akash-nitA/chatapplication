package com.example.chatApp.Utility;

import com.example.chatApp.Services.Impl.UserDetailsServiceImpl;
import com.example.chatApp.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
//    private static final Logger log= LoggerFactory.getLogger(JwtAuthFilter.class);
    public JwtAuthFilter(UserDetailsService userDetailsService,JwtService jwtService){
        this.userDetailsService=userDetailsService;
        this.jwtService=jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader= request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            try {
                username=jwtService.extractUserName(token);
            } catch (Exception ignored) {
                username = null;
            }
        }
//        log.info("isAuthenticated value: "+SecurityContextHolder.getContext().getAuthentication());
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(token,user)){
                    var authToken= new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception ignored) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request,response);
    }
}
