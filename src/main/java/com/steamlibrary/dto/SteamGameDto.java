package com.steamlibrary.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SteamGameDto {
    private Long appId;
    private String name;
    private Integer playtimeForever;
    private Integer playtime2weeks;
    private String imgIconUrl;
    private String imgLogoUrl;
}
