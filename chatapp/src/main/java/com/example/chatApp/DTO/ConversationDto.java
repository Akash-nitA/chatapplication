package com.example.chatApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {
    private Integer id;
    private String message;
    private String status;
    private String sender;
    private String receiver;
}
