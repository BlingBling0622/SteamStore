package com.steamlibrary.controller;

import com.steamlibrary.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StoreService storeService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", storeService.getFeaturedProducts());
        model.addAttribute("allProducts", storeService.getAllProducts());
        return "index";
    }
}
