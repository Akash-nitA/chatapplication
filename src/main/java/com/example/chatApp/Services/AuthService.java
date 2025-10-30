package com.example.chatApp.Services;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.example.chatApp.DTO.StudentDto;
import com.example.chatApp.Models.Students;

public interface AuthService {
	ResponseEntity<?> createUser(StudentDto student);
	
}
