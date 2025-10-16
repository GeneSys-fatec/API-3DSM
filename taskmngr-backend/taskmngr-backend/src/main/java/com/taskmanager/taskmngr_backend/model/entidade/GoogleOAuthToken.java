package com.taskmanager.taskmngr_backend.model.entidade;

import java.time.Instant;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "google_oauth_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleOAuthToken {

    @Id
    private String id;

    // username/email do usuário autenticado
    @Indexed(unique = true)
    private String userKey;

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String scope;

    // epoch em milissegundos
    private Long expiryDate;

    private Instant createdAt;
    private Instant updatedAt;

    public void touchOnCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void touchOnUpdate() {
        this.updatedAt = Instant.now();
    }
}