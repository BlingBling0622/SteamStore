package com.steamlibrary.repository;

import com.steamlibrary.model.GroupChat;
import com.steamlibrary.model.Message;
import com.steamlibrary.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) " +
           "OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt DESC")
    List<Message> findConversation(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT DISTINCT CASE WHEN m.sender = :user THEN m.receiver ELSE m.sender END " +
           "FROM Message m WHERE (m.sender = :user OR m.receiver = :user) AND m.type = 'CHAT'")
    List<User> findConversationPartners(@Param("user") User user);

    long countByReceiverAndIsReadFalse(User receiver);

    long countBySenderAndReceiverAndIsReadFalse(User sender, User receiver);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.sender = :sender AND m.receiver = :receiver AND m.isRead = false")
    int markAsRead(@Param("sender") User sender, @Param("receiver") User receiver);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) " +
           "OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt DESC")
    List<Message> findLatestBetween(@Param("user1") User user1, @Param("user2") User user2, Pageable pageable);

    List<Message> findByReceiverAndTypeAndSenderIsNull(User receiver, Message.MessageType type);

    List<Message> findByReceiverAndSenderIsNullOrderByCreatedAtDesc(User receiver);

    List<Message> findByGroupOrderByCreatedAtAsc(GroupChat group);
}
