package com.example.chatApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.example.chatApp.Services.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
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
	SecurityFilterChain securityFilterChain(HttpSecurity http,DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
		return http
				.csrf(csrf -> csrf
						.disable())
				.authorizeHttpRequests(authorize-> authorize
				.requestMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
				)
				.authenticationProvider(daoAuthenticationProvider)
				
				.build();
	}
}
