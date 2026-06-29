package com.steamlibrary.config;

import com.steamlibrary.model.User;
import com.steamlibrary.service.FriendService;
import com.steamlibrary.service.MessageService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final FriendService friendService;
    private final MessageService messageService;
    private final UserService userService;

    @ModelAttribute
    public void addCommonAttributes(Locale locale, Model model, Authentication authentication) {
        // Currency settings
        model.addAttribute("currentLang", locale.getLanguage());
        switch (locale.getLanguage()) {
            case "ja" -> { model.addAttribute("cSym", "¥");  model.addAttribute("cRate", 155.0);  model.addAttribute("cDec", 0); }
            case "ko" -> { model.addAttribute("cSym", "₩");  model.addAttribute("cRate", 1370.0); model.addAttribute("cDec", 0); }
            case "ru" -> { model.addAttribute("cSym", "₽");  model.addAttribute("cRate", 89.0);   model.addAttribute("cDec", 0); }
            case "fr", "de", "es" ->
                         { model.addAttribute("cSym", "€");  model.addAttribute("cRate", 0.92);   model.addAttribute("cDec", 2); }
            case "zh" -> { model.addAttribute("cSym", "¥");  model.addAttribute("cRate", 7.25);   model.addAttribute("cDec", 2); }
            default   -> { model.addAttribute("cSym", "$");  model.addAttribute("cRate", 1.0);    model.addAttribute("cDec", 2); }
        }

        // Badge counts for authenticated users
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            try {
                User user = userService.findByUsername(username);
                model.addAttribute("pendingFriendCount", friendService.getPendingCount(user));
                model.addAttribute("unreadMessageCount", messageService.getUnreadCount(user));
            } catch (Exception ignored) {
                model.addAttribute("pendingFriendCount", 0L);
                model.addAttribute("unreadMessageCount", 0L);
            }
        } else {
            model.addAttribute("pendingFriendCount", 0L);
            model.addAttribute("unreadMessageCount", 0L);
        }
    }
}
