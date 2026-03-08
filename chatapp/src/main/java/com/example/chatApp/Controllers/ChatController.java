package com.example.chatApp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.example.chatApp.DTO.MessageBody;
import com.example.chatApp.Services.ChatService;

import java.security.Principal;

@RestController
@RequestMapping("/chat")
public class ChatController {
	public final ChatService chatService;
	public ChatController(ChatService chatService) {
		this.chatService = chatService;
		
	}
	@PostMapping("/send")
	ResponseEntity<?> sendMessage(@RequestBody MessageBody message, Principal principal){
		
		ResponseEntity<?> reply=chatService.sendMessage(message, principal.getName());
		if(reply.getStatusCode()==HttpStatus.valueOf(404)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body("Message Sent Succesfully");
	}

	@GetMapping("/messages")
	ResponseEntity<?> getAllMessages(
			Principal principal,
			@RequestParam(name = "with", required = false) String otherUsername
	){
		String username = principal.getName();
		if (otherUsername != null && !otherUsername.isBlank()) {
			return ResponseEntity.ok().body(chatService.getConversation(username, otherUsername));
		}
		return ResponseEntity.ok().body(chatService.getAllMessage(username));
	}

	@GetMapping("/users")
	ResponseEntity<?> getChatUsers(Principal principal){
		return ResponseEntity.ok().body(chatService.getChatUsers(principal.getName()));
	}
	

}
