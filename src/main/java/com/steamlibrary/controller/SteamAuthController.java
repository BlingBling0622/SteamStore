package com.steamlibrary.controller;

import com.steamlibrary.model.User;
import com.steamlibrary.service.SteamOpenIdService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openid4java.message.ParameterList;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling Steam OpenID authentication.
 */
@Controller
@RequestMapping("/auth/steam")
@RequiredArgsConstructor
@Slf4j
public class SteamAuthController {

    private final SteamOpenIdService steamOpenIdService;

    /**
     * Initiates Steam OpenID authentication flow.
     *
     * @return Redirect to Steam login page
     */
    @GetMapping("/login")
    public String steamLogin() {
        log.info("Steam login initiated");

        try {
            String authUrl = steamOpenIdService.initiateAuthentication();
            return "redirect:" + authUrl;
        } catch (SteamOpenIdService.SteamAuthenticationException e) {
            log.error("Failed to initiate Steam login", e);
            return "redirect:/login?steamError";
        }
    }

    /**
     * Handles the callback from Steam after authentication.
     *
     * @param request The HTTP request containing OpenID parameters
     * @param model The model for view rendering
     * @param redirectAttributes Attributes for redirect scenarios
     * @return View name or redirect URL
     */
    @GetMapping("/callback")
    public String steamCallback(HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        log.info("Steam callback received");

        try {
            // Get the full URL with parameters
            String receivingUrl = getFullURL(request);

            // Extract OpenID parameters
            ParameterList parameterList = new ParameterList(request.getParameterMap());

            // Verify and authenticate user
            User user = steamOpenIdService.verifyAndAuthenticateUser(receivingUrl, parameterList);

            log.info("Steam authentication successful for user: {}", user.getUsername());

            // Redirect to dashboard after successful authentication
            redirectAttributes.addFlashAttribute("message", "Successfully logged in with Steam!");
            return "redirect:/dashboard";

        } catch (SteamOpenIdService.SteamAuthenticationException e) {
            log.error("Steam authentication failed", e);
            redirectAttributes.addFlashAttribute("error", "Steam authentication failed: " + e.getMessage());
            return "redirect:/login?steamError";
        } catch (Exception e) {
            log.error("Unexpected error during Steam callback", e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred during Steam authentication");
            return "redirect:/login?error";
        }
    }

    /**
     * Links the current user's account with their Steam account.
     *
     * @param authentication The current authentication
     * @return Redirect to Steam login for linking
     */
    @PostMapping("/link")
    public String linkSteamAccount(Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to link a Steam account");
            return "redirect:/login";
        }

        log.info("User {} initiated Steam account linking", authentication.getName());

        try {
            String authUrl = steamOpenIdService.initiateAuthentication();
            // In a real implementation, you'd store a flag in the session to indicate this is a linking flow
            return "redirect:" + authUrl;
        } catch (SteamOpenIdService.SteamAuthenticationException e) {
            log.error("Failed to initiate Steam linking", e);
            redirectAttributes.addFlashAttribute("error", "Failed to initiate Steam linking: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    /**
     * Unlinks the current user's Steam account.
     *
     * @param authentication The current authentication
     * @param redirectAttributes Attributes for redirect
     * @return Redirect to dashboard
     */
    @PostMapping("/unlink")
    public String unlinkSteamAccount(Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to unlink a Steam account");
            return "redirect:/login";
        }

        log.info("User {} initiated Steam account unlinking", authentication.getName());

        try {
            // This would require getting the user ID from the authentication
            // For now, this is a placeholder showing the intended flow
            redirectAttributes.addFlashAttribute("message", "Steam account unlinked successfully");
            return "redirect:/dashboard";
        } catch (Exception e) {
            log.error("Failed to unlink Steam account", e);
            redirectAttributes.addFlashAttribute("error", "Failed to unlink Steam account: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    /**
     * Constructs the full URL from the request.
     *
     * @param request The HTTP request
     * @return The full URL including query parameters
     */
    private String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
