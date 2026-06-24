package com.steamlibrary.controller;

import com.steamlibrary.model.User;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FileUploadController {

    private final UserService userService;
    private static final Path UPLOAD_DIR = Paths.get("uploads");

    static {
        try {
            Files.createDirectories(UPLOAD_DIR.resolve("avatars"));
            Files.createDirectories(UPLOAD_DIR.resolve("backgrounds"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directories", e);
        }
    }

    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file,
                               Authentication authentication,
                               RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Please select a file");
            return "redirect:/profile/edit";
        }
        try {
            String filename = "avatar_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path dest = UPLOAD_DIR.resolve("avatars").resolve(filename);
            file.transferTo(dest);

            User user = userService.findByUsername(authentication.getName());
            user.setAvatarUrl("/uploads/avatars/" + filename);
            userService.save(user);

            ra.addFlashAttribute("success", "Avatar uploaded!");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile/edit";
    }

    @PostMapping("/profile/upload-background")
    public String uploadBackground(@RequestParam("file") MultipartFile file,
                                   Authentication authentication,
                                   RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Please select a file");
            return "redirect:/profile/edit";
        }
        try {
            String filename = "bg_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path dest = UPLOAD_DIR.resolve("backgrounds").resolve(filename);
            file.transferTo(dest);

            User user = userService.findByUsername(authentication.getName());
            user.setProfileBackgroundUrl("/uploads/backgrounds/" + filename);
            userService.save(user);

            ra.addFlashAttribute("success", "Background updated!");
        } catch (IOException e) {
            ra.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile/edit";
    }
}
