package com.steamlibrary.config;

import com.steamlibrary.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Best-effort, throttled writer of {@code User.lastSeenAt} on every authenticated
 * request. Drives the online/offline indicator shown on the friends page.
 *
 * <p>Each authenticated request resolves the current user and writes lastSeenAt — but
 * throttled to once every {@link #THROTTLE_MS} per user so the DB isn't hammered on
 * rapid requests (asset/CSS/JS traffic is excluded at the registry level too).
 *
 * <p>This interceptor MUST NEVER block a request: any failure (anonymous user, DB error,
 * missing user) falls through with {@code return true}.
 */
@Component
@RequiredArgsConstructor
public class UserActivityInterceptor implements HandlerInterceptor {

    private final UserService userService;

    /** Min interval between two lastSeenAt writes for the same user. */
    private static final long THROTTLE_MS = 10_000L;

    /** userId → epoch millis of the last successful write. */
    private final ConcurrentHashMap<Long, Long> lastWrite = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                return true;
            }
            String username = auth.getName();
            if (username == null || username.isBlank()) return true;
            var user = userService.findByUsername(username);
            if (user == null) return true;
            Long id = user.getId();
            if (id == null) return true;

            long now = System.currentTimeMillis();
            Long prev = lastWrite.get(id);
            if (prev != null && (now - prev) < THROTTLE_MS) {
                return true; // throttled — skip this write
            }
            lastWrite.put(id, now);
            userService.updateLastSeenAt(id);
        } catch (Exception ignored) {
            // best-effort: never block the request
        }
        return true;
    }
}
