package com.example.chatApp.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chatApp.Models.Conversations;
import com.example.chatApp.Models.Students;

public interface ChatRepo extends JpaRepository<Conversations,Integer>{
    List<Conversations> findByReciever_Id(Long receiverId);
    List<Conversations> findBySender_IdOrReciever_IdOrderByIdAsc(Integer senderId, Integer receiverId);

    @Query("""
            SELECT c
            FROM Conversations c
            WHERE (c.sender = :currentUser AND c.reciever = :otherUser)
               OR (c.sender = :otherUser AND c.reciever = :currentUser)
            ORDER BY c.id ASC
            """)
    List<Conversations> findConversationBetweenUsers(
            @Param("currentUser") Students currentUser,
            @Param("otherUser") Students otherUser
    );
}
