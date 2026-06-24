package com.steamlibrary.controller;

import com.steamlibrary.model.User;
import com.steamlibrary.repository.UserRepository;
import com.steamlibrary.service.StoreService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class DebugController {

    private final UserService userService;
    private final StoreService storeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/debug/session")
    @ResponseBody
    public Map<String, Object> debugSession(Authentication auth) {
        Map<String, Object> debug = new HashMap<>();
        if (auth == null) {
            debug.put("authenticated", false);
        } else {
            debug.put("authenticated", true);
            debug.put("username", auth.getName());
            try {
                User user = userService.findByUsername(auth.getName());
                debug.put("userId", user.getId());
                debug.put("email", user.getEmail());
                debug.put("cartCount", storeService.getCart(user).size());
            } catch (Exception e) {
                debug.put("error", e.getMessage());
            }
        }
        return debug;
    }

    @GetMapping("/debug/check-user")
    @ResponseBody
    public Map<String, Object> checkUser(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) {
            result.put("found", false);
            result.put("error", "User not found");
            return result;
        }
        User user = opt.get();
        result.put("found", true);
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("storedPasswordPrefix", user.getPassword().substring(0, Math.min(20, user.getPassword().length())));
        result.put("passwordMatch", passwordEncoder.matches(password, user.getPassword()));
        result.put("passwordLength", user.getPassword().length());
        return result;
    }
}
