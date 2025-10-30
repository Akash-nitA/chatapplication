package com.example.chatApp.Services.Impl;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.chatApp.DTO.MessageBody;
import com.example.chatApp.Models.Conversations;
import com.example.chatApp.Models.Students;
import com.example.chatApp.Repository.AuthRepo;
import com.example.chatApp.Repository.ChatRepo;
import com.example.chatApp.Services.ChatService;

import com.example.chatApp.Repository.ChatRepo;

@Service
public class ChatServiceImpl implements ChatService{

    private final ChatRepo chatRepo;
    private final AuthRepo authRepo;

    ChatServiceImpl(ChatRepo chatRepo, AuthRepo authRepo) {
        this.chatRepo = chatRepo;
		this.authRepo = authRepo;
    }

	@Override
	public ResponseEntity<?> sendMessage(MessageBody message) {
		Optional<Students> reciever= authRepo.findOneByName(message.getReciever());
		Optional<Students> sender=authRepo.findOneByName(message.getSender());
		if(reciever.isEmpty()) return ResponseEntity.notFound().build();
		Conversations conversation=new Conversations();
		conversation.setReciever(reciever.get());
		conversation.setSender(sender.get());
		conversation.setMessage(message.getMessage());
		conversation.setStatus("delivered");
		chatRepo.save(conversation);
		return ResponseEntity.ok().build();
	}

}
