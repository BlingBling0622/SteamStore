package com.steamlibrary.controller;

import com.steamlibrary.model.Order;
import com.steamlibrary.model.Product;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    @GetMapping("/store")
    public String store(@RequestParam(required = false) String q, Model model, Authentication auth) {
        // Exclude DLCs from main listing, but include in search
        if (q != null && !q.isBlank()) {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(q.trim()));
            model.addAttribute("searchQuery", q.trim());
        } else {
            model.addAttribute("products", productRepository.findByIsDlcFalse());
        }
        if (auth != null) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("ownedIds", storeService.getOwnedProductIds(user));
            model.addAttribute("cartCount", storeService.getCart(user).size());
        }
        return "store";
    }

    @GetMapping("/store/game/{id}")
    public String gameDetail(@PathVariable Long id, Model model, Authentication auth) {
        Product product = storeService.getProduct(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        model.addAttribute("product", product);

        // DLCs for this game
        if (product.getIsDlc() != null && !product.getIsDlc()) {
            model.addAttribute("dlcs", productRepository.findByParentGameId(id));
        }

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
        model.addAttribute("cartTotal", storeService.getCartTotal(items));
        model.addAttribute("cartCount", items.size());
        model.addAttribute("ownedIds", storeService.getOwnedProductIds(user));
        return "cart";
    }

    @PostMapping("/store/claim-free")
    public String claimFree(@RequestParam Long productId, Authentication auth,
                            RedirectAttributes ra) {
        User user = userService.findByUsername(auth.getName());
        storeService.claimFree(user, productId);
        ra.addFlashAttribute("success", "Game added to your library!");
        return "redirect:/store/game/" + productId;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, Authentication auth,
                            RedirectAttributes ra) {
        User user = userService.findByUsername(auth.getName());
        storeService.addToCart(user, productId);
        return "redirect:/cart";
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
