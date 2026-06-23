package com.steamlibrary.controller;

import com.steamlibrary.model.User;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StoreService storeService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        model.addAttribute("featured", storeService.getFeaturedProducts());
        model.addAttribute("allProducts", storeService.getAllProducts());

        if (auth != null) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("ownedIds", storeService.getOwnedProductIds(user));
            model.addAttribute("cartCount", storeService.getCart(user).size());
        }

        return "index";
    }
}
