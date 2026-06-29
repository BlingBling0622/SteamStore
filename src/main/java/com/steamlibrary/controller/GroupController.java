package com.steamlibrary.controller;

import com.steamlibrary.model.*;
import com.steamlibrary.service.*;
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
public class GroupController {

    private final GroupService groupService;
    private final FriendService friendService;
    private final UserService userService;

    @GetMapping("/groups")
    @Transactional(readOnly = true)
    public String groupsPage(Authentication authentication, Model model,
                             @RequestParam(required = false) Long chat) {
        User user = userService.findByUsername(authentication.getName());
        List<GroupChat> groups = groupService.getUserGroups(user);
        List<User> friends = friendService.getFriends(user);

        // Convert groups to maps for Thymeleaf
        List<Map<String, Object>> groupViews = new ArrayList<>();
        for (GroupChat g : groups) {
            Map<String, Object> gm = new LinkedHashMap<>();
            gm.put("id", g.getId());
            gm.put("name", g.getName());
            gm.put("memberCount", groupService.getMembers(g.getId()).size());
            groupViews.add(gm);
        }

        // Convert friends to maps
        List<Map<String, Object>> friendViews = new ArrayList<>();
        for (User f : friends) {
            Map<String, Object> fm = new LinkedHashMap<>();
            fm.put("id", f.getId());
            fm.put("username", f.getUsername());
            fm.put("nickname", f.getNickname() != null ? f.getNickname() : f.getUsername());
            fm.put("avatarUrl", f.getAvatarUrl() != null ? f.getAvatarUrl() : "");
            friendViews.add(fm);
        }

        model.addAttribute("user", user);
        model.addAttribute("groups", groupViews);
        model.addAttribute("friends", friendViews);

        if (chat != null) {
            try {
                GroupChat group = groupService.getGroup(chat);
                model.addAttribute("activeGroup", group);
                model.addAttribute("activeGroupId", group.getId());
                model.addAttribute("members", groupService.getMembers(chat));
            } catch (Exception ignored) {}
        }

        return "groups";
    }

    @PostMapping("/groups/create")
    public String createGroup(@RequestParam String name,
                              Authentication authentication,
                              RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            GroupChat group = groupService.createGroup(name, user);
            ra.addFlashAttribute("success", "Group '" + group.getName() + "' created!");
            return "redirect:/groups?chat=" + group.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/groups";
        }
    }

    @PostMapping("/groups/invite")
    public String inviteUser(@RequestParam Long groupId,
                             @RequestParam Long userId,
                             Authentication authentication,
                             RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            groupService.inviteUser(groupId, user, userId);
            ra.addFlashAttribute("success", "User invited!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups?chat=" + groupId;
    }

    @PostMapping("/groups/remove")
    public String removeMember(@RequestParam Long groupId,
                               @RequestParam Long userId,
                               Authentication authentication,
                               RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        try {
            groupService.removeMember(groupId, user, userId);
            ra.addFlashAttribute("success", "Member removed.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/groups?chat=" + groupId;
    }

    @PostMapping("/groups/send")
    @ResponseBody
    public Map<String, Object> sendGroupMessage(@RequestParam Long groupId,
                                                 @RequestParam String content,
                                                 Authentication authentication) {
        User sender = userService.findByUsername(authentication.getName());
        try {
            Message msg = groupService.sendGroupMessage(groupId, sender, content);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", msg.getId());
            result.put("content", msg.getContent());
            result.put("createdAt", msg.getCreatedAt().toString());
            result.put("senderId", msg.getSender().getId());
            result.put("senderUsername", msg.getSender().getUsername());
            result.put("senderNickname", msg.getSender().getNickname() != null ? msg.getSender().getNickname() : msg.getSender().getUsername());
            result.put("senderAvatarUrl", msg.getSender().getAvatarUrl() != null ? msg.getSender().getAvatarUrl() : "");
            result.put("success", true);
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    @GetMapping("/groups/messages/{groupId}")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getGroupMessages(@PathVariable Long groupId,
                                                        Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return groupService.getGroupMessages(groupId, user).stream()
                .map(m -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", m.getId());
                    map.put("content", m.getContent());
                    map.put("senderId", m.getSender().getId());
                    map.put("senderUsername", m.getSender().getUsername());
                    map.put("senderNickname", m.getSender().getNickname() != null ? m.getSender().getNickname() : m.getSender().getUsername());
                    map.put("senderAvatarUrl", m.getSender().getAvatarUrl() != null ? m.getSender().getAvatarUrl() : "");
                    map.put("createdAt", m.getCreatedAt().toString());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/groups/members/{groupId}")
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMembers(@PathVariable Long groupId) {
        return groupService.getMembers(groupId).stream()
                .map(m -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", m.getUser().getId());
                    map.put("username", m.getUser().getUsername());
                    map.put("nickname", m.getUser().getNickname() != null ? m.getUser().getNickname() : m.getUser().getUsername());
                    map.put("avatarUrl", m.getUser().getAvatarUrl() != null ? m.getUser().getAvatarUrl() : "");
                    map.put("role", m.getRole().name());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/groups/friends-search")
    @ResponseBody
    public List<Map<String, Object>> searchFriendsNotInGroup(@RequestParam Long groupId,
                                                               @RequestParam String q,
                                                               Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<User> friends = friendService.getFriends(user);
        List<GroupMember> members = groupService.getMembers(groupId);
        Set<Long> memberIds = members.stream().map(m -> m.getUser().getId()).collect(Collectors.toSet());

        return friends.stream()
                .filter(f -> !memberIds.contains(f.getId()))
                .filter(f -> f.getUsername().toLowerCase().contains(q.toLowerCase())
                        || (f.getNickname() != null && f.getNickname().toLowerCase().contains(q.toLowerCase())))
                .map(f -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", f.getId());
                    m.put("username", f.getUsername());
                    m.put("nickname", f.getNickname() != null ? f.getNickname() : f.getUsername());
                    m.put("avatarUrl", f.getAvatarUrl() != null ? f.getAvatarUrl() : "");
                    return m;
                })
                .collect(Collectors.toList());
    }
}
