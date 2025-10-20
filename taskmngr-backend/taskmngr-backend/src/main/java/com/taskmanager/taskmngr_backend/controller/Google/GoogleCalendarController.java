package com.taskmanager.taskmngr_backend.controller.Google;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.GoogleOAuthService;

@RestController
@RequestMapping("/google")
public class GoogleCalendarController {

    private final GoogleOAuthService googleOAuthService;

    public GoogleCalendarController(GoogleOAuthService googleOAuthService) {
        this.googleOAuthService = googleOAuthService;
    }

    private String currentUserKey() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("unauthorized");
        }
        return auth.getName();
    }

    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(@RequestBody Map<String, Object> body) {
        String code = body != null ? (String) body.get("code") : null;
        String redirectUri = body != null && body.get("redirectUri") != null ? String.valueOf(body.get("redirectUri")) : null;

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_code"));
        }

        googleOAuthService.exchangeCode(currentUserKey(), code, redirectUri);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        boolean has = googleOAuthService.hasTokens(currentUserKey());
        return ResponseEntity.ok(Map.of("loggedIn", has));
    }

    @GetMapping("/events")
    public ResponseEntity<?> events(@RequestParam(required = false) String timeMin,
                                    @RequestParam(required = false) String timeMax) {
        return googleOAuthService.listEvents(currentUserKey(), timeMin, timeMax);
    }

    @PostMapping("/create-event")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> eventBody) {
        return googleOAuthService.createEvent(currentUserKey(), eventBody);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        return googleOAuthService.deleteEvent(currentUserKey(), eventId);
    }
}