package com.example.chatApp.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private LocalDateTime createdAt;
    @JsonIgnore
	Set<String> Roles;
}
