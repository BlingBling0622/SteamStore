package com.steamlibrary.repository;

import com.steamlibrary.model.SteamAccount;
import com.steamlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SteamAccountRepository extends JpaRepository<SteamAccount, Long> {

    Optional<SteamAccount> findByUser(User user);

    Optional<SteamAccount> findBySteamId(String steamId);

    boolean existsBySteamId(String steamId);

    boolean existsByUser(User user);
}
