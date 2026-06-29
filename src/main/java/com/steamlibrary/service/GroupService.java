package com.steamlibrary.service;

import com.steamlibrary.model.*;
import com.steamlibrary.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupChatRepository groupChatRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupChat createGroup(String name, User creator) {
        GroupChat group = new GroupChat();
        group.setName(name.trim());
        group.setCreator(creator);
        group = groupChatRepository.save(group);

        // Creator is auto-joined as OWNER
        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(creator);
        member.setRole(GroupMember.Role.OWNER);
        groupMemberRepository.save(member);

        return group;
    }

    @Transactional
    public void inviteUser(Long groupId, User inviter, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check inviter is a member
        if (!groupMemberRepository.existsByGroupAndUser(group, inviter)) {
            throw new RuntimeException("You are not a member of this group");
        }
        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("User is already in this group");
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setRole(GroupMember.Role.MEMBER);
        groupMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(Long groupId, User remover, Long userId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        GroupMember removerMember = groupMemberRepository.findByGroupAndUser(group, remover)
                .orElseThrow(() -> new RuntimeException("You are not a member of this group"));
        if (removerMember.getRole() == GroupMember.Role.MEMBER) {
            throw new RuntimeException("Only owner or admin can remove members");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        GroupMember target = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new RuntimeException("User is not in this group"));
        if (target.getRole() == GroupMember.Role.OWNER) {
            throw new RuntimeException("Cannot remove the group owner");
        }

        groupMemberRepository.delete(target);
    }

    @Transactional
    public Message sendGroupMessage(Long groupId, User sender, String content) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!groupMemberRepository.existsByGroupAndUser(group, sender)) {
            throw new RuntimeException("You are not a member of this group");
        }
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Message cannot be empty");
        }

        Message msg = new Message();
        msg.setSender(sender);
        msg.setGroup(group);
        msg.setContent(content.trim());
        msg.setType(Message.MessageType.GROUP);
        msg.setIsRead(false);
        return messageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public List<GroupChat> getUserGroups(User user) {
        return groupMemberRepository.findByUser(user).stream()
                .map(GroupMember::getGroup)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GroupMember> getMembers(Long groupId) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMemberRepository.findByGroup(group);
    }

    @Transactional(readOnly = true)
    public List<Message> getGroupMessages(Long groupId, User user) {
        GroupChat group = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        if (!groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new RuntimeException("You are not a member of this group");
        }
        return messageRepository.findByGroupOrderByCreatedAtAsc(group);
    }

    @Transactional(readOnly = true)
    public GroupChat getGroup(Long groupId) {
        return groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
}
