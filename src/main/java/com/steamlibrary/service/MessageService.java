package com.steamlibrary.service;

import com.steamlibrary.model.Message;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.MessageRepository;
import com.steamlibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FriendService friendService;

    @Transactional
    public Message sendMessage(User sender, Long receiverId, String content) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Message cannot be empty");
        }
        if (!friendService.areFriends(sender, receiver)) {
            throw new RuntimeException("You can only send messages to friends");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content.trim());
        message.setType(Message.MessageType.CHAT);
        message.setIsRead(false);
        return messageRepository.save(message);
    }

    @Transactional
    public void createNotification(User user, String content, Message.MessageType type) {
        Message msg = new Message();
        msg.setSender(null); // system notification
        msg.setReceiver(user);
        msg.setContent(content);
        msg.setType(type);
        msg.setIsRead(false);
        messageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public List<Message> getConversation(User user, User otherUser) {
        return messageRepository.findConversation(user, otherUser);
    }

    @Transactional(readOnly = true)
    public List<User> getConversationPartners(User user) {
        return messageRepository.findConversationPartners(user);
    }

    @Transactional
    public void markAsRead(User sender, User receiver) {
        messageRepository.markAsRead(sender, receiver);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    @Transactional(readOnly = true)
    public long getUnreadCountFrom(User sender, User receiver) {
        return messageRepository.countBySenderAndReceiverAndIsReadFalse(sender, receiver);
    }

    @Transactional(readOnly = true)
    public List<Message> getNotifications(User user) {
        return messageRepository.findByReceiverAndSenderIsNullOrderByCreatedAtDesc(user);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId, User user) {
        messageRepository.findById(notificationId).ifPresent(msg -> {
            if (msg.getReceiver().getId().equals(user.getId()) && msg.getSender() == null) {
                msg.setIsRead(true);
                messageRepository.save(msg);
            }
        });
    }

    @Transactional
    public void deleteNotification(Long notificationId, User user) {
        messageRepository.findById(notificationId).ifPresent(msg -> {
            if (msg.getReceiver().getId().equals(user.getId()) && msg.getSender() == null) {
                messageRepository.delete(msg);
            }
        });
    }
}
