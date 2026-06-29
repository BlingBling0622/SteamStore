package com.steamlibrary.repository;

import com.steamlibrary.model.FriendRequest;
import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequest.FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatus(User sender, FriendRequest.FriendRequestStatus status);

    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender = :sender OR fr.receiver = :receiver) AND fr.status = :status")
    List<FriendRequest> findBySenderOrReceiverAndStatus(@Param("sender") User sender, @Param("receiver") User receiver, @Param("status") FriendRequest.FriendRequestStatus status);

    long countByReceiverAndStatus(User receiver, FriendRequest.FriendRequestStatus status);

    Optional<FriendRequest> findBySenderAndReceiverAndStatus(User sender, User receiver, FriendRequest.FriendRequestStatus status);

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
}
