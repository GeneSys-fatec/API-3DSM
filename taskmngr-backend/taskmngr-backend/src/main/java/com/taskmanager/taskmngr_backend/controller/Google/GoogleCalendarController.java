package com.taskmanager.taskmngr_backend.controller.Google;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.Google.GoogleOAuthService;

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
        String redirectUri = body != null && body.get("redirectUri") != null
                ? String.valueOf(body.get("redirectUri"))
                : null;

        if (code == null || code.isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "missing_code", "message", "O código de autorização não foi informado."));
        }

        googleOAuthService.exchangeCode(currentUserKey(), code, redirectUri);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("message", "Conta Google conectada com sucesso."));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        boolean has = googleOAuthService.hasTokens(currentUserKey());
        if (has) {
            return ResponseEntity.ok(Map.of(
                    "loggedIn", true,
                    "message", "Usuário conectado à conta Google."));
        } else {
            return ResponseEntity.ok(Map.of(
                    "loggedIn", false,
                    "message", "Usuário ainda não conectou uma conta Google."));
        }
    }

    @GetMapping("/events")
    public ResponseEntity<?> events(@RequestParam(required = false) String timeMin,
                                    @RequestParam(required = false) String timeMax) {
        var response = googleOAuthService.listEvents(currentUserKey(), timeMin, timeMax);
        return ResponseEntity.ok(Map.of(
                "message", "Eventos obtidos com sucesso.",
                "data", response.getBody()));
    }

    @PostMapping("/create-event")
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> eventBody) {
        var response = googleOAuthService.createEvent(currentUserKey(), eventBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Evento criado com sucesso no Google Calendar.",
                        "data", response.getBody()));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        googleOAuthService.deleteEvent(currentUserKey(), eventId);
        return ResponseEntity.ok(Map.of(
                "message", "Evento excluído com sucesso do Google Calendar."));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable String eventId, @RequestBody Map<String, Object> eventBody) {
        var response = googleOAuthService.updateEvent(currentUserKey(), eventId, eventBody);
        return ResponseEntity.ok(Map.of(
                "message", "Evento atualizado com sucesso no Google Calendar.",
                "data", response.getBody()));
    }
}
