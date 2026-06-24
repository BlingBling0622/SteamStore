package com.steamlibrary.repository;

import com.steamlibrary.model.Game;
import com.steamlibrary.model.SteamAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findBySteamAccount(SteamAccount steamAccount);

    Optional<Game> findBySteamAccountAndAppId(SteamAccount steamAccount, String appId);

    void deleteBySteamAccount(SteamAccount steamAccount);

    long countBySteamAccount(SteamAccount steamAccount);
}
