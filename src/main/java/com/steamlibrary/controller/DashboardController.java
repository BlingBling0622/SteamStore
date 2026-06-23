package com.steamlibrary.controller;

import com.steamlibrary.model.SteamAccount;
import com.steamlibrary.model.SteamGame;
import com.steamlibrary.model.User;
import com.steamlibrary.service.SteamService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final SteamService steamService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        Optional<SteamAccount> steamAccount = steamService.getSteamAccountByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("steamLinked", steamAccount.isPresent());

        if (steamAccount.isPresent()) {
            SteamAccount account = steamAccount.get();
            List<SteamGame> library = steamService.getUserLibrary(account.getSteamId());

            model.addAttribute("steamAccount", account);
            model.addAttribute("gameCount", library.size());
            model.addAttribute("library", library);

            // Calculate total playtime
            int totalPlaytime = library.stream()
                    .mapToInt(game -> game.getPlaytimeForever() != null ? game.getPlaytimeForever() : 0)
                    .sum();
            model.addAttribute("totalPlaytime", totalPlaytime);
        }

        return "dashboard";
    }
}
