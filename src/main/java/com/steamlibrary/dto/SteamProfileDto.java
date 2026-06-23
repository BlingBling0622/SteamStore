package com.steamlibrary.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SteamProfileDto {
    private String steamId;
    private String personaName;
    private String profileUrl;
    private String avatarUrl;
}
