package com.steamlibrary.repository;

import com.steamlibrary.model.GroupChat;
import com.steamlibrary.model.GroupMember;
import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroup(GroupChat group);

    List<GroupMember> findByUser(User user);

    Optional<GroupMember> findByGroupAndUser(GroupChat group, User user);

    boolean existsByGroupAndUser(GroupChat group, User user);

    void deleteByGroupAndUser(GroupChat group, User user);
}
