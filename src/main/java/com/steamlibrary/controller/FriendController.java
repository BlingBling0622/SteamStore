package com.steamlibrary.controller;

import com.steamlibrary.model.FriendRequest;
import com.steamlibrary.model.Product;
import com.steamlibrary.model.User;
import com.steamlibrary.service.FriendService;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;
    private final StoreService storeService;

    @GetMapping("/friends")
    @Transactional(readOnly = true)
    public String friendsPage(Authentication authentication, Model model) {
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
            fm.put("email", f.getEmail());
            fm.put("since", f.getCreatedAt() != null ? f.getCreatedAt().toString() : "");
            fm.put("updatedAt", f.getUpdatedAt() != null ? f.getUpdatedAt().toString() : "");
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
        model.addAttribute("friendsCount", friendViews.size());
        model.addAttribute("pendingCount", pendingViews.size());
        return "friends";
    }

    @PostMapping("/friends/send")
    public String sendRequest(@RequestParam Long receiverId,
                              Authentication authentication,
                              RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            friendService.sendRequest(user, receiverId);
            ra.addFlashAttribute("success", "Friend request sent!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/friends";
    }

    @PostMapping("/friends/accept")
    public String acceptRequest(@RequestParam Long requestId,
                                Authentication authentication,
                                RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            friendService.acceptRequest(requestId, user);
            ra.addFlashAttribute("success", "Friend request accepted!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/friends";
    }

    @PostMapping("/friends/decline")
    public String declineRequest(@RequestParam Long requestId,
                                 Authentication authentication,
                                 RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            friendService.declineRequest(requestId, user);
            ra.addFlashAttribute("success", "Friend request declined.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/friends";
    }

    @PostMapping("/friends/remove")
    public String removeFriend(@RequestParam Long friendId,
                               Authentication authentication,
                               RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            friendService.removeFriend(user, friendId);
            ra.addFlashAttribute("success", "Friend removed.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/friends";
    }

    @GetMapping("/friends/{friendId}")
    @Transactional(readOnly = true)
    public String friendProfilePage(@PathVariable Long friendId,
                                     Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        User friend = userService.findById(friendId);
        if (!friendService.areFriends(currentUser, friend)) {
            return "redirect:/friends";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("friend", friend);
        model.addAttribute("friendNickname", friend.getNickname() != null ? friend.getNickname() : friend.getUsername());
        model.addAttribute("friendAvatar", friend.getAvatarUrl());
        model.addAttribute("friendBg", friend.getProfileBackgroundUrl());
        model.addAttribute("friendEmail", friend.getEmail());
        model.addAttribute("friendSince", friend.getCreatedAt());
        model.addAttribute("friendSteamId", friend.getSteamId());

        List<Product> friendGames = storeService.getPurchasedProducts(friend);
        model.addAttribute("friendGames", friendGames);
        model.addAttribute("friendGameCount", friendGames.size());
        model.addAttribute("friendTotalSpent", storeService.getTotalSpent(friend));
        model.addAttribute("friendCount", friendService.getFriends(friend).size());

        return "friend-profile";
    }

    @GetMapping("/friends/profile/{friendId}")
    @ResponseBody
    @Transactional(readOnly = true)
    public Map<String, Object> friendProfile(@PathVariable Long friendId,
                                              Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User friend = userService.findById(friendId);
        if (!friendService.areFriends(currentUser, friend)) {
            throw new RuntimeException("Not friends with this user");
        }

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("id", friend.getId());
        profile.put("username", friend.getUsername());
        profile.put("nickname", friend.getNickname() != null ? friend.getNickname() : friend.getUsername());
        profile.put("avatarUrl", friend.getAvatarUrl() != null ? friend.getAvatarUrl() : "");
        profile.put("email", friend.getEmail());
        profile.put("memberSince", friend.getCreatedAt() != null ? friend.getCreatedAt().toString() : "");
        profile.put("steamId", friend.getSteamId() != null ? friend.getSteamId() : "");
        profile.put("gameCount", storeService.getPurchasedProducts(friend).size());
        profile.put("totalSpent", storeService.getTotalSpent(friend));

        return profile;
    }

    @GetMapping("/friends/search")
    @ResponseBody
    public List<Map<String, Object>> searchUsers(@RequestParam String q,
                                                   Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return friendService.searchUsers(q, user).stream()
                .map(u -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", u.getId());
                    m.put("username", u.getUsername());
                    m.put("nickname", u.getNickname() != null ? u.getNickname() : u.getUsername());
                    m.put("avatarUrl", u.getAvatarUrl() != null ? u.getAvatarUrl() : "");
                    return m;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/friends/pending-count")
    @ResponseBody
    public Map<String, Object> getPendingCount(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("count", friendService.getPendingCount(user));
        return result;
    }
}
