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

    private final GoogleOAuthService service;

    public GoogleCalendarController(GoogleOAuthService service) {
        this.service = service;
    }

    private String currentUserKey() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("unauthorized");
        }
        return auth.getName(); // username/email do usuário autenticado
    }

    @PostMapping("/exchange")
    public ResponseEntity<?> exchange(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String redirectUri = body.get("redirectUri");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "code_required"));
        }
        service.exchangeCode(currentUserKey(), code, redirectUri);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        boolean has = service.hasTokens(currentUserKey());
        return ResponseEntity.ok(Map.of("loggedIn", has));
    }

    @GetMapping("/events")
    public ResponseEntity<?> events(@RequestParam(required = false) String timeMin,
                                    @RequestParam(required = false) String timeMax) {
        return service.listEvents(currentUserKey(), timeMin, timeMax);
    }

    @PostMapping("/create-event")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> eventBody) {
        return service.createEvent(currentUserKey(), eventBody);
    }

    // DELETE /google/events/{eventId} -> remove o evento no Google Calendar
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        return service.deleteEvent(currentUserKey(), eventId);
    }
}