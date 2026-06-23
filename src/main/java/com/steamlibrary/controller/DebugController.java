package com.steamlibrary.controller;

import com.steamlibrary.model.Order;
import com.steamlibrary.model.User;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class DebugController {

    private final UserService userService;
    private final StoreService storeService;

    @GetMapping("/debug/session")
    @ResponseBody
    public Map<String, Object> debugSession(Authentication auth) {
        Map<String, Object> debug = new HashMap<>();

        if (auth == null) {
            debug.put("authenticated", false);
            debug.put("message", "Not logged in");
        } else {
            debug.put("authenticated", true);
            debug.put("username", auth.getName());
            debug.put("principal", auth.getPrincipal().getClass().getSimpleName());

            try {
                User user = userService.findByUsername(auth.getName());
                debug.put("userId", user.getId());
                debug.put("email", user.getEmail());

                Set<Long> ownedIds = storeService.getOwnedProductIds(user);
                debug.put("ownedProductCount", ownedIds.size());
                debug.put("ownedProductIds", ownedIds);

                debug.put("cartCount", storeService.getCart(user).size());
            } catch (Exception e) {
                debug.put("error", e.getMessage());
                debug.put("errorClass", e.getClass().getSimpleName());
            }
        }

        return debug;
    }
}
