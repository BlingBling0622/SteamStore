package com.steamlibrary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "steam_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SteamAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "steam_id", unique = true, nullable = false)
    private String steamId;

    @Column(name = "persona_name")
    private String personaName;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "linked_at")
    private LocalDateTime linkedAt;

    @Column(name = "last_synced")
    private LocalDateTime lastSynced;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        linkedAt = LocalDateTime.now();
        lastSynced = LocalDateTime.now();
    }
}
