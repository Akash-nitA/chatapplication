package com.example.chatApp.Services;
import com.example.chatApp.Models.Conversations;
import org.springframework.http.ResponseEntity;
import com.example.chatApp.DTO.MessageBody;

import java.util.List;

public interface ChatService {
	ResponseEntity<?> sendMessage(MessageBody message);
    List<Conversations> getAllMessage(String username);
}
