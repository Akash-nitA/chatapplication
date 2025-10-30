package com.example.chatApp.Services;

import java.util.Optional;

import com.example.chatApp.Models.Students;

public interface UserDetailsService {
	boolean existByName(String name);
	Optional<Students> findByName(String name);
}
