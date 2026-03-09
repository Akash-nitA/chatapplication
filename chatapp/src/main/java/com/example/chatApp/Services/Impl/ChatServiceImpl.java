package com.example.chatApp.Services.Impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.chatApp.DTO.ChatUserDto;
import com.example.chatApp.DTO.ConversationDto;
import com.example.chatApp.DTO.MessageBody;
import com.example.chatApp.Models.Conversations;
import com.example.chatApp.Models.Students;
import com.example.chatApp.Repository.AuthRepo;
import com.example.chatApp.Repository.ChatRepo;
import com.example.chatApp.Services.ChatService;
import com.example.chatApp.Utility.ChatWebSocketHandler;

@Service
public class ChatServiceImpl implements ChatService{

    private final ChatRepo chatRepo;
    private final AuthRepo authRepo;
    private final ChatWebSocketHandler chatWebSocketHandler;

    ChatServiceImpl(ChatRepo chatRepo, AuthRepo authRepo, ChatWebSocketHandler chatWebSocketHandler) {
        this.chatRepo = chatRepo;
		this.authRepo = authRepo;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

	@Override
	public ResponseEntity<?> sendMessage(MessageBody message, String senderUsername) {
		Optional<Students> receiver= authRepo.findOneByName(message.getReciever());
		Optional<Students> sender=authRepo.findOneByName(senderUsername);
		if(receiver.isEmpty() || sender.isEmpty()) return ResponseEntity.notFound().build();
		Conversations conversation=new Conversations();
		conversation.setReciever(receiver.get());
		conversation.setSender(sender.get());
			conversation.setMessage(message.getMessage());
			conversation.setStatus("delivered");
			chatRepo.save(conversation);
            chatWebSocketHandler.notifyUsers(sender.get().getName(), receiver.get().getName(), message.getMessage());
			return ResponseEntity.ok().build();
		}
	@Override
	public List<ConversationDto> getAllMessage(String username){
		Optional<Students> currentUser = authRepo.findOneByName(username);
		if (currentUser.isEmpty()) {
			return Collections.emptyList();
		}
		Integer currentUserId = currentUser.get().getId();
		return chatRepo.findBySender_IdOrReciever_IdOrderByIdAsc(currentUserId, currentUserId)
				.stream()
				.map(this::toConversationDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<ConversationDto> getConversation(String username, String otherUsername){
		Optional<Students> currentUser = authRepo.findOneByName(username);
		Optional<Students> otherUser = authRepo.findOneByName(otherUsername);
		if (currentUser.isEmpty() || otherUser.isEmpty()) {
			return Collections.emptyList();
		}

		return chatRepo.findConversationBetweenUsers(currentUser.get(), otherUser.get())
				.stream()
				.map(this::toConversationDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<ChatUserDto> getChatUsers(String username){
		return authRepo.findByNameNotOrderByNameAsc(username)
				.stream()
				.map(user -> new ChatUserDto(user.getId(), user.getName(), user.getEmail()))
				.collect(Collectors.toList());
	}

	private ConversationDto toConversationDto(Conversations conversation) {
		return new ConversationDto(
				conversation.getId(),
				conversation.getMessage(),
				conversation.getStatus(),
				conversation.getSender().getName(),
				conversation.getReciever().getName()
		);
	}

}
