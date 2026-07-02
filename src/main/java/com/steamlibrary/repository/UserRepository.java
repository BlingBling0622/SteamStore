package com.steamlibrary.repository;

import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findBySteamId(String steamId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByUsernameContainingIgnoreCase(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(u.nickname, '')) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<User> searchByUsernameOrNickname(@Param("q") String q);

    /** Bulk-update lastSeenAt via JPQL — bypasses @PreUpdate so updatedAt is untouched. */
    @Modifying
    @Query("UPDATE User u SET u.lastSeenAt = :now WHERE u.id = :id")
    int updateLastSeenAt(@Param("id") Long id, @Param("now") LocalDateTime now);

    /** Mark user offline immediately (e.g. on logout / page close). */
    @Modifying
    @Query("UPDATE User u SET u.lastSeenAt = NULL WHERE u.id = :id")
    int clearLastSeenAt(@Param("id") Long id);
}
