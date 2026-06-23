package com.steamlibrary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "steam_games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SteamGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id", nullable = false)
    private Long appId;

    @Column(name = "steam_id", nullable = false)
    private String steamId;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "playtime_forever")
    private Integer playtimeForever; // in minutes

    @Column(name = "playtime_2weeks")
    private Integer playtime2weeks; // in minutes

    @Column(name = "img_icon_url")
    private String imgIconUrl;

    @Column(name = "img_logo_url")
    private String imgLogoUrl;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        syncedAt = LocalDateTime.now();
    }
}
