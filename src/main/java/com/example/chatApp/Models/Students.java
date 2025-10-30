package com.example.chatApp.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Students {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int Id;
	@NonNull
	private String name;
	@NonNull
	@Column(unique=true)
	private String email;
	@NonNull
	private String password;
	private LocalDateTime createdAt;
	Set<String> Roles;
}
