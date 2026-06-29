package com.steamlibrary.service;

import com.steamlibrary.model.FriendRequest;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.FriendRequestRepository;
import com.steamlibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendRequest sendRequest(User sender, Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (sender.getId().equals(receiverId)) {
            throw new RuntimeException("Cannot send friend request to yourself");
        }

        // Check if already sent a pending request
        if (friendRequestRepository.findBySenderAndReceiverAndStatus(
                sender, receiver, FriendRequest.FriendRequestStatus.PENDING).isPresent()) {
            throw new RuntimeException("Friend request already sent");
        }

        // Check if request exists in opposite direction (pending)
        if (friendRequestRepository.findBySenderAndReceiverAndStatus(
                receiver, sender, FriendRequest.FriendRequestStatus.PENDING).isPresent()) {
            throw new RuntimeException("This user has already sent you a friend request");
        }

        // Check if already friends
        if (friendRequestRepository.findBySenderAndReceiverAndStatus(
                sender, receiver, FriendRequest.FriendRequestStatus.ACCEPTED).isPresent() ||
            friendRequestRepository.findBySenderAndReceiverAndStatus(
                receiver, sender, FriendRequest.FriendRequestStatus.ACCEPTED).isPresent()) {
            throw new RuntimeException("You are already friends with this user");
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequest.FriendRequestStatus.PENDING);
        return friendRequestRepository.save(request);
    }

    @Transactional
    public void acceptRequest(Long requestId, User currentUser) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to accept this request");
        }
        if (request.getStatus() != FriendRequest.FriendRequestStatus.PENDING) {
            throw new RuntimeException("This request is no longer pending");
        }
        request.setStatus(FriendRequest.FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }

    @Transactional
    public void declineRequest(Long requestId, User currentUser) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to decline this request");
        }
        if (request.getStatus() != FriendRequest.FriendRequestStatus.PENDING) {
            throw new RuntimeException("This request is no longer pending");
        }
        request.setStatus(FriendRequest.FriendRequestStatus.DECLINED);
        friendRequestRepository.save(request);
    }

    @Transactional
    public void removeFriend(User currentUser, Long friendId) {
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find accepted request in either direction
        friendRequestRepository.findBySenderAndReceiverAndStatus(
                currentUser, friend, FriendRequest.FriendRequestStatus.ACCEPTED)
                .ifPresent(friendRequestRepository::delete);

        friendRequestRepository.findBySenderAndReceiverAndStatus(
                friend, currentUser, FriendRequest.FriendRequestStatus.ACCEPTED)
                .ifPresent(friendRequestRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<User> getFriends(User user) {
        List<FriendRequest> accepted = friendRequestRepository
                .findBySenderOrReceiverAndStatus(user, user, FriendRequest.FriendRequestStatus.ACCEPTED);
        return accepted.stream()
                .map(r -> r.getSender().getId().equals(user.getId()) ? r.getReceiver() : r.getSender())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> getPendingRequests(User user) {
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequest.FriendRequestStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public long getPendingCount(User user) {
        return friendRequestRepository.countByReceiverAndStatus(user, FriendRequest.FriendRequestStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<User> searchUsers(String query, User currentUser) {
        return userRepository.searchByUsernameOrNickname(query).stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean areFriends(User user1, User user2) {
        return friendRequestRepository.findBySenderAndReceiverAndStatus(
                user1, user2, FriendRequest.FriendRequestStatus.ACCEPTED).isPresent()
            || friendRequestRepository.findBySenderAndReceiverAndStatus(
                user2, user1, FriendRequest.FriendRequestStatus.ACCEPTED).isPresent();
    }
}
