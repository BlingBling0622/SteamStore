package com.steamlibrary.controller;

import com.steamlibrary.model.Product;
import com.steamlibrary.model.User;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final StoreService storeService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);

        List<Product> purchasedGames = storeService.getPurchasedProducts(user);
        model.addAttribute("purchasedGames", purchasedGames);
        model.addAttribute("gameCount", purchasedGames.size());
        model.addAttribute("totalSpent", storeService.getTotalSpent(user));

        // Parse favorite IDs
        Set<Long> favIds = new HashSet<>();
        if (user.getFavoriteIds() != null && !user.getFavoriteIds().isBlank()) {
            for (String s : user.getFavoriteIds().split(",")) {
                try { favIds.add(Long.parseLong(s.trim())); } catch (NumberFormatException ignored) {}
            }
        }
        model.addAttribute("favoriteIds", favIds);

        // All unique tags from purchased games for collections sidebar
        Set<String> allTags = new LinkedHashSet<>();
        for (Product p : purchasedGames) {
            if (p.getTags() != null) {
                for (String tag : p.getTags().split(",")) {
                    String t = tag.trim();
                    if (!t.isBlank()) allTags.add(t);
                }
            }
        }
        model.addAttribute("allTags", allTags);

        return "dashboard";
    }

    @PostMapping("/dashboard/favorite")
    @ResponseBody
    public Map<String, Object> toggleFavorite(@RequestParam Long productId, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Set<Long> favs = new LinkedHashSet<>();
        if (user.getFavoriteIds() != null && !user.getFavoriteIds().isBlank()) {
            for (String s : user.getFavoriteIds().split(",")) {
                try { favs.add(Long.parseLong(s.trim())); } catch (NumberFormatException ignored) {}
            }
        }
        boolean added;
        if (favs.contains(productId)) {
            favs.remove(productId);
            added = false;
        } else {
            favs.add(productId);
            added = true;
        }
        user.setFavoriteIds(String.join(",", favs.stream().map(String::valueOf).toList()));
        userService.save(user);
        return Map.of("added", added, "count", favs.size());
    }
}
