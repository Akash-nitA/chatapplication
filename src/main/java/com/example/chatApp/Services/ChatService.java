package com.example.chatApp.Services;
import org.springframework.http.ResponseEntity;
import com.example.chatApp.DTO.MessageBody;

public interface ChatService {
	ResponseEntity<?> sendMessage(MessageBody message);
}
