package com.example.chatApp.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
	private String name;
	private String email;
	private String password;
	private String createdAt;
	private List<String> Roles;
	
}
