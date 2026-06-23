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

import java.util.List;

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

        // Get purchased store games
        List<Product> purchasedGames = storeService.getPurchasedProducts(user);
        model.addAttribute("purchasedGames", purchasedGames);
        model.addAttribute("gameCount", purchasedGames.size());

        return "dashboard";
    }
}
