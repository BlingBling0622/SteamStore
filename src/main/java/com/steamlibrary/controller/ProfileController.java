package com.steamlibrary.controller;

import com.steamlibrary.model.Product;
import com.steamlibrary.model.SteamAccount;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.SteamAccountRepository;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final StoreService storeService;
    private final SteamAccountRepository steamAccountRepository;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        List<Product> purchasedGames = storeService.getPurchasedProducts(user);
        model.addAttribute("purchasedGames", purchasedGames);
        model.addAttribute("gameCount", purchasedGames.size());
        model.addAttribute("totalSpent", storeService.getTotalSpent(user));
        model.addAttribute("memberSince", user.getCreatedAt());

        Optional<SteamAccount> steamAccount = steamAccountRepository.findByUser(user);
        model.addAttribute("steamAccount", steamAccount.orElse(null));
        model.addAttribute("steamLinked", steamAccount.isPresent());

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        // Background options from user's purchased games
        List<Product> purchased = storeService.getPurchasedProducts(user);
        List<String> purchasedScreenshots = purchased.stream()
                .filter(p -> p.getScreenshot1Url() != null && !p.getScreenshot1Url().isBlank())
                .map(Product::getScreenshot1Url)
                .distinct()
                .limit(20)
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("purchasedScreenshots", purchasedScreenshots);

        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String saveEdit(@RequestParam(required = false) String nickname,
                           @RequestParam(required = false) String avatarUrl,
                           @RequestParam(required = false) String backgroundUrl,
                           Authentication authentication,
                           RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        if (nickname != null && !nickname.isBlank()) user.setNickname(nickname.trim());
        if (avatarUrl != null && !avatarUrl.isBlank()) user.setAvatarUrl(avatarUrl.trim());
        if (backgroundUrl != null && !backgroundUrl.isBlank()) user.setProfileBackgroundUrl(backgroundUrl.trim());
        userService.save(user);
        ra.addFlashAttribute("success", "Profile updated!");
        return "redirect:/profile/edit";
    }

    @PostMapping("/profile/background")
    public String updateBackground(@RequestParam String backgroundUrl,
                                   Authentication authentication,
                                   RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        user.setProfileBackgroundUrl(backgroundUrl);
        userService.save(user);
        ra.addFlashAttribute("success", "Background updated!");
        return "redirect:/profile/edit";
    }

    @PostMapping("/profile/clear-avatar")
    public String clearAvatar(Authentication authentication, RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        user.setAvatarUrl(null);
        userService.save(user);
        ra.addFlashAttribute("success", "Avatar removed!");
        return "redirect:/profile/edit";
    }

    @PostMapping("/profile/clear-background")
    public String clearBackground(Authentication authentication, RedirectAttributes ra) {
        User user = userService.findByUsername(authentication.getName());
        user.setProfileBackgroundUrl(null);
        userService.save(user);
        ra.addFlashAttribute("success", "Background removed!");
        return "redirect:/profile/edit";
    }
}
