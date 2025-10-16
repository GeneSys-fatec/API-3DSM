package com.taskmanager.taskmngr_backend.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.taskmanager.taskmngr_backend.model.entidade.GoogleOAuthToken;
import com.taskmanager.taskmngr_backend.repository.GoogleOAuthTokenRepository;

@Service
public class GoogleOAuthService {

    private final GoogleOAuthTokenRepository repo;
    private final RestTemplate http = new RestTemplate();

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri:}")
    private String redirectUri;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String EVENTS_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    public GoogleOAuthService(GoogleOAuthTokenRepository repo) {
        this.repo = repo;
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    private static long withSkew(long expiresInSec) {
        return now() + (expiresInSec * 1000L) - 30_000L; // -30s de margem
    }

    public void exchangeCode(String userKey, String code, String bodyRedirectUri) {
        String useRedirect = (bodyRedirectUri != null && !bodyRedirectUri.isBlank())
                ? bodyRedirectUri
                : (redirectUri == null || redirectUri.isBlank() ? "postmessage" : redirectUri);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("grant_type", "authorization_code");
        form.add("redirect_uri", useRedirect);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> resp = http.postForEntity(TOKEN_URL, new HttpEntity<>(form, headers), Map.class);
        Map body = resp.getBody();

        String accessToken = (String) body.get("access_token");
        String refreshToken = (String) body.get("refresh_token"); // pode vir só na 1ª troca
        String tokenType = (String) body.get("token_type");
        String scope = (String) body.get("scope");
        Number expiresIn = (Number) body.get("expires_in");
        long expiryDate = withSkew(expiresIn != null ? expiresIn.longValue() : 0L);

        GoogleOAuthToken token = repo.findByUserKey(userKey).orElseGet(GoogleOAuthToken::new);
        boolean isNew = (token.getId() == null);
        token.setUserKey(userKey);
        token.setAccessToken(accessToken);
        if (refreshToken != null && !refreshToken.isBlank()) token.setRefreshToken(refreshToken);
        token.setTokenType(tokenType);
        token.setScope(scope);
        token.setExpiryDate(expiryDate);
        if (isNew) token.touchOnCreate(); else token.touchOnUpdate();
        repo.save(token);
    }

    public boolean hasTokens(String userKey) {
        return repo.existsByUserKey(userKey);
    }

    private void refreshAccessToken(GoogleOAuthToken token) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", token.getRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> resp = http.postForEntity(TOKEN_URL, new HttpEntity<>(form, headers), Map.class);
        Map body = resp.getBody();

        String accessToken = (String) body.get("access_token");
        String tokenType = (String) body.get("token_type");
        String scope = (String) body.get("scope");
        Number expiresIn = (Number) body.get("expires_in");
        long expiryDate = withSkew(expiresIn != null ? expiresIn.longValue() : 0L);

        token.setAccessToken(accessToken);
        token.setTokenType(tokenType);
        token.setScope(scope);
        token.setExpiryDate(expiryDate);
        token.touchOnUpdate();
        repo.save(token);
    }

    private String getValidAccessToken(String userKey) {
        GoogleOAuthToken token = repo.findByUserKey(userKey)
                .orElseThrow(() -> new IllegalStateException("no_tokens"));
        if (token.getExpiryDate() != null && token.getExpiryDate() > now()) {
            return token.getAccessToken();
        }
        if (token.getRefreshToken() == null || token.getRefreshToken().isBlank()) {
            throw new IllegalStateException("no_refresh_token");
        }
        refreshAccessToken(token);
        return repo.findByUserKey(userKey).get().getAccessToken();
    }

    public ResponseEntity<Map> listEvents(String userKey, String timeMin, String timeMax) {
        String access = getValidAccessToken(userKey);

        StringBuilder url = new StringBuilder(EVENTS_URL)
                .append("?singleEvents=true&orderBy=startTime&showDeleted=false&maxResults=2500");
        if (timeMin != null && !timeMin.isBlank()) url.append("&timeMin=").append(timeMin);
        if (timeMax != null && !timeMax.isBlank()) url.append("&timeMax=").append(timeMax);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(access);

        try {
            return http.exchange(url.toString(), HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                GoogleOAuthToken saved = repo.findByUserKey(userKey).orElseThrow();
                if (saved.getRefreshToken() != null && !saved.getRefreshToken().isBlank()) {
                    refreshAccessToken(saved);
                    headers.setBearerAuth(getValidAccessToken(userKey));
                    return http.exchange(url.toString(), HttpMethod.GET, new HttpEntity<>(headers), Map.class);
                }
            }
            throw ex;
        }
    }

    public ResponseEntity<Map> createEvent(String userKey, Map<String, Object> eventBody) {
        String access = getValidAccessToken(userKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(access);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            return http.exchange(EVENTS_URL, HttpMethod.POST, new HttpEntity<>(eventBody, headers), Map.class);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                GoogleOAuthToken saved = repo.findByUserKey(userKey).orElseThrow();
                if (saved.getRefreshToken() != null && !saved.getRefreshToken().isBlank()) {
                    refreshAccessToken(saved);
                    headers.setBearerAuth(getValidAccessToken(userKey));
                    return http.exchange(EVENTS_URL, HttpMethod.POST, new HttpEntity<>(eventBody, headers), Map.class);
                }
            }
            throw ex;
        }
    }

    // Remove um evento do Google Calendar pelo eventId
    public ResponseEntity<Void> deleteEvent(String userKey, String eventId) {
        String access = getValidAccessToken(userKey);

        String url = EVENTS_URL + "/" + URLEncoder.encode(eventId, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(access);

        try {
            return http.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        } catch (RestClientResponseException ex) {
            // Se o token expirou, tenta uma vez atualizar e repetir

            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // força refresh obtendo novamente um access válido
                access = getValidAccessToken(userKey);
                headers.setBearerAuth(access);
                return http.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
            }
            // Se o evento não existir, responde 204 (idempotente)
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.noContent().build();
            }
            throw ex;
        }
    }
}