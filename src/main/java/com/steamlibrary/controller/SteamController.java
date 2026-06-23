package com.steamlibrary.controller;

import com.steamlibrary.model.User;
import com.steamlibrary.service.SteamOpenIdService;
import com.steamlibrary.service.SteamService;
import com.steamlibrary.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/steam")
@RequiredArgsConstructor
public class SteamController {

    private final SteamOpenIdService steamOpenIdService;
    private final SteamService steamService;
    private final UserService userService;

    @GetMapping("/link")
    public String linkSteamAccount(RedirectAttributes redirectAttributes) {
        try {
            String loginUrl = steamOpenIdService.getLoginUrl();
            return "redirect:" + loginUrl;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "Failed to connect to Steam. Please check your internet connection and try again. Error: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/callback")
    public String steamCallback(HttpServletRequest request,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            String steamId = steamOpenIdService.verifySteamLogin(request);
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            steamService.linkSteamAccount(user, steamId);
            steamService.syncSteamLibrary(steamId);

            redirectAttributes.addFlashAttribute("success", "Steam account linked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to link Steam account: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/sync")
    public String syncLibrary(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            steamService.getSteamAccountByUser(user).ifPresent(account -> {
                steamService.syncSteamLibrary(account.getSteamId());
            });

            redirectAttributes.addFlashAttribute("success", "Library synced successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to sync library: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}
