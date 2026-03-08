package com.example.chatApp.Services;
import com.example.chatApp.DTO.ChatUserDto;
import com.example.chatApp.DTO.ConversationDto;
import org.springframework.http.ResponseEntity;
import com.example.chatApp.DTO.MessageBody;

import java.util.List;

public interface ChatService {
	ResponseEntity<?> sendMessage(MessageBody message, String senderUsername);
    List<ConversationDto> getAllMessage(String username);
    List<ConversationDto> getConversation(String username, String otherUsername);
    List<ChatUserDto> getChatUsers(String username);
}
