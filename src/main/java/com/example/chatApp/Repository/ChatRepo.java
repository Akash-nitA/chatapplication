package com.example.chatApp.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatApp.Models.Conversations;

public interface ChatRepo extends JpaRepository<Conversations,Integer>{
	
}
