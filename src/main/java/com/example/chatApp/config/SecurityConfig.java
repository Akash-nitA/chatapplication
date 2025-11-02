package com.example.chatApp.config;

import com.example.chatApp.Services.Impl.UserDetailsServiceImpl;
import com.example.chatApp.Services.JwtService;
import com.example.chatApp.Utility.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsServiceImpl userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);
	        return provider;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthFilter jwtAuthFilter(UserDetailsService userDetailsService , JwtService jwtService){
        return new JwtAuthFilter(userDetailsService,jwtService);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager();}
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,DaoAuthenticationProvider daoAuthenticationProvider, JwtAuthFilter jwtAuthFilter) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize-> authorize
				.requestMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
				)
				.authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				
				.build();
	}
}
