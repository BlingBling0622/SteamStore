package com.steamlibrary.controller;

import com.steamlibrary.model.FriendRequest;
import com.steamlibrary.model.Message;
import com.steamlibrary.model.User;
import com.steamlibrary.service.FriendService;
import com.steamlibrary.service.MessageService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final FriendService friendService;
    private final UserService userService;

    @GetMapping("/messages")
    @Transactional(readOnly = true)
    public String messagesPage(Authentication authentication, Model model,
                               @RequestParam(required = false) Long chat) {
        User user = userService.findByUsername(authentication.getName());
        List<User> friends = friendService.getFriends(user);
        List<FriendRequest> pendingRequests = friendService.getPendingRequests(user);

        // Convert friends to plain maps to avoid LazyInitializationException in Thymeleaf
        List<Map<String, Object>> friendViews = new ArrayList<>();
        for (User f : friends) {
            Map<String, Object> fm = new LinkedHashMap<>();
            fm.put("id", f.getId());
            fm.put("username", f.getUsername());
            fm.put("nickname", f.getNickname() != null ? f.getNickname() : f.getUsername());
            fm.put("avatarUrl", f.getAvatarUrl() != null ? f.getAvatarUrl() : "");
            friendViews.add(fm);
        }

        // Convert pending requests to plain maps to avoid LazyInitializationException in Thymeleaf
        List<Map<String, Object>> pendingViews = new ArrayList<>();
        for (FriendRequest req : pendingRequests) {
            Map<String, Object> view = new LinkedHashMap<>();
            view.put("id", req.getId());
            view.put("senderId", req.getSender().getId());
            view.put("senderUsername", req.getSender().getUsername());
            view.put("senderNickname", req.getSender().getNickname() != null ? req.getSender().getNickname() : req.getSender().getUsername());
            view.put("senderAvatarUrl", req.getSender().getAvatarUrl());
            view.put("createdAt", req.getCreatedAt());
            view.put("status", req.getStatus().name());
            pendingViews.add(view);
        }

        model.addAttribute("user", user);
        model.addAttribute("friends", friendViews);
        model.addAttribute("pendingRequests", pendingViews);

        // Load system notifications (purchase + library)
        List<Message> notifications = messageService.getNotifications(user);
        List<Map<String, Object>> notifViews = new ArrayList<>();
        for (Message n : notifications) {
            Map<String, Object> nm = new LinkedHashMap<>();
            nm.put("id", n.getId());
            nm.put("content", n.getContent());
            nm.put("type", n.getType().name());
            nm.put("createdAt", n.getCreatedAt());
            nm.put("isRead", n.getIsRead());
            notifViews.add(nm);
        }
        model.addAttribute("notifications", notifViews);

        // If a chat target is specified, load that conversation
        if (chat != null) {
            try {
                User chatFriend = userService.findById(chat);
                model.addAttribute("chatFriend", chatFriend);
                List<Message> conversation = messageService.getConversation(user, chatFriend);
                model.addAttribute("conversation", conversation);
            } catch (Exception ignored) {}
        }

        return "messages";
    }

    @PostMapping("/messages/send")
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestParam Long receiverId,
                                            @RequestParam String content,
                                            Authentication authentication) {
        User sender = userService.findByUsername(authentication.getName());
        try {
            Message msg = messageService.sendMessage(sender, receiverId, content);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", msg.getId());
            result.put("content", msg.getContent());
            result.put("createdAt", msg.getCreatedAt().toString());
            result.put("senderId", msg.getSender().getId());
            result.put("success", true);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    @GetMapping("/messages/conversation/{friendId}")
    @ResponseBody
    @Transactional
    public List<Map<String, Object>> getConversation(@PathVariable Long friendId,
                                                       Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        User friend = userService.findById(friendId);
        messageService.markAsRead(friend, user);
        return messageService.getConversation(user, friend).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .map(m -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", m.getId());
                    map.put("content", m.getContent());
                    map.put("senderId", m.getSender().getId());
                    map.put("senderUsername", m.getSender().getUsername());
                    map.put("senderAvatarUrl", m.getSender().getAvatarUrl() != null ? m.getSender().getAvatarUrl() : "");
                    map.put("createdAt", m.getCreatedAt().toString());
                    map.put("isRead", m.getIsRead());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/messages/notification/read")
    @ResponseBody
    @Transactional
    public Map<String, Object> readNotification(@RequestParam Long notificationId,
                                                  Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        messageService.markNotificationAsRead(notificationId, user);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("count", messageService.getUnreadCount(user));
        return result;
    }

    @PostMapping("/messages/notification/dismiss")
    @ResponseBody
    @Transactional
    public Map<String, Object> dismissNotification(@RequestParam Long notificationId,
                                                     Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            messageService.deleteNotification(notificationId, user);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
        }
        return result;
    }

    @GetMapping("/messages/unread-count")
    @ResponseBody
    public Map<String, Object> getUnreadCount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", messageService.getUnreadCount(user));
        return result;
    }
}
