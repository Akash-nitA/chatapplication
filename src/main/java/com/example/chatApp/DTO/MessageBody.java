package com.example.chatApp.DTO;

import com.example.chatApp.Models.Students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
	private String message;
	private String status;
	private String sender;
	private String reciever;
}
