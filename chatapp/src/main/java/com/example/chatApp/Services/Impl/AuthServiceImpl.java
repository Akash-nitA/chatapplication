package com.example.chatApp.Services.Impl;

import java.net.PasswordAuthentication;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chatApp.DTO.StudentDto;
import com.example.chatApp.Models.Students;
import com.example.chatApp.Repository.AuthRepo;
import com.example.chatApp.Services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
	private final AuthRepo authRepo;
	private final Logger logger=LoggerFactory.getLogger(AuthServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
	public AuthServiceImpl(AuthRepo authRepo,PasswordEncoder passwordEncoder) {
		this.authRepo = authRepo;
//		this.logger = null;
        this.passwordEncoder=passwordEncoder;
		
	}

	@Override
	public ResponseEntity<?> createUser(StudentDto student) {
		// TODO Auto-generated method stub
		Optional<Students> Student=authRepo.findOneByName(student.getName());
		
		if(Student.isPresent()) {
			ResponseEntity<?> response=ResponseEntity.status(409).body("User Already Exists");
			logger.info("custom log value"+response);
			return response;
		} 
		Students newStudent=new Students();
		newStudent.setName(student.getName());
		newStudent.setEmail(student.getEmail());
		newStudent.setPassword(passwordEncoder.encode(student.getPassword()));
		newStudent.setCreatedAt(LocalDateTime.now());
//		newStudent.setRoles(new List<String>());
		authRepo.save(newStudent);
		return ResponseEntity.ok().body("User created SuccessFully");
	}

}
