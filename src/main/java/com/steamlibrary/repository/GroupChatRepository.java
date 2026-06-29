package com.steamlibrary.repository;

import com.steamlibrary.model.GroupChat;
import com.steamlibrary.model.GroupMember;
import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    List<GroupChat> findByCreator(User creator);
}
