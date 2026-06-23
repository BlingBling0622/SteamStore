package com.steamlibrary.repository;

import com.steamlibrary.model.SteamAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SteamAccountRepository extends JpaRepository<SteamAccount, Long> {

    Optional<SteamAccount> findBySteamId(String steamId);

    Optional<SteamAccount> findByUserId(Long userId);

    boolean existsBySteamId(String steamId);
}
