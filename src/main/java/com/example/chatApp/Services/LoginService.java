package com.example.chatApp.Services;

import com.example.chatApp.Services.Impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    LoginService(AuthenticationManager authenticationManager,
                 UserDetailsServiceImpl userDetailsService,
                 JwtService jwtService){
        this.authenticationManager=authenticationManager;
        this.userDetailsService=userDetailsService;
        this.jwtService=jwtService;
    }

    public String login(String userName,String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        UserDetails user= userDetailsService.loadUserByUsername(userName);
        return jwtService.generateToken(user);
    }
}
