package com.steamlibrary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "games", indexes = {
    @Index(name = "idx_steam_account_id", columnList = "steam_account_id"),
    @Index(name = "idx_app_id", columnList = "appId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "steam_account_id", nullable = false)
    private SteamAccount steamAccount;

    @Column(nullable = false)
    private String appId;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer playtimeForever;

    @Column
    private Integer playtimeWindowsForever;

    @Column
    private Integer playtimeMacForever;

    @Column
    private Integer playtimeLinuxForever;

    @Column
    private String imgIconUrl;

    @Column
    private String imgLogoUrl;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @PrePersist
    protected void onCreate() {
        syncedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return id != null && id.equals(game.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
