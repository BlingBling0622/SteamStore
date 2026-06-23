package com.steamlibrary.controller;

import com.steamlibrary.model.Order;
import com.steamlibrary.model.User;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @GetMapping("/store")
    public String store(Model model, Authentication auth) {
        model.addAttribute("products", storeService.getAllProducts());
        if (auth != null) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("ownedIds", storeService.getOwnedProductIds(user));
            model.addAttribute("cartCount", storeService.getCart(user).size());
        }
        return "store";
    }

    @GetMapping("/store/game/{id}")
    public String gameDetail(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute("product", storeService.getProduct(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
        if (auth != null) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("owned", storeService.getOwnedProductIds(user).contains(id));
            model.addAttribute("inCart", storeService.getCart(user).stream()
                    .anyMatch(c -> c.getProduct().getId().equals(id)));
            model.addAttribute("cartCount", storeService.getCart(user).size());
        }
        return "game-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        var items = storeService.getCart(user);
        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", storeService.getCartTotal(user));
        model.addAttribute("cartCount", items.size());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, Authentication auth,
                            RedirectAttributes ra) {
        User user = userService.findByUsername(auth.getName());
        storeService.addToCart(user, productId);
        ra.addFlashAttribute("success", "Added to cart!");
        return "redirect:/store/game/" + productId;
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        storeService.removeFromCart(user, productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        Order order = storeService.checkout(user);
        model.addAttribute("order", order);
        return "checkout-success";
    }
}
