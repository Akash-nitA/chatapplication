package com.example.chatApp.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatApp.Models.Conversations;

public interface ChatRepo extends JpaRepository<Conversations,Integer>{
    List<Conversations> findByReciever_Id(Long receiverId);
}
