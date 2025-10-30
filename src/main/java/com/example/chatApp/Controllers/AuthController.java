package com.example.chatApp.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatApp.DTO.StudentDto;
import com.example.chatApp.Services.AuthService;
import com.example.chatApp.Services.UserDetailsService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	private final AuthService authService;
	private final UserDetailsService userDetails;
	public AuthController(AuthService authService,UserDetailsService userDetails) {
		this.authService=authService;
		this.userDetails=userDetails;
	}
	
	@PostMapping("/register")
	ResponseEntity<?> registerStudent(@RequestBody StudentDto studentDto){
		ResponseEntity<?> response=authService.createUser(studentDto);
		Map<String,String> responseObj=new HashMap<>();
		log.info("user status: "+userDetails.existByName(studentDto.getName()));
		if(response.getStatusCode()==HttpStatus.valueOf(409)) { 
			responseObj.put("message", "User with this username already exists");
			return ResponseEntity.status(409).body(responseObj);
		}
		
		responseObj.put("message", "user registration successfull");
		
		return ResponseEntity.ok(response);
	}

}
