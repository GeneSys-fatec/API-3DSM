package com.taskmanager.taskmngr_backend.segurancaFiltro;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.Auth.CookieService;
import com.taskmanager.taskmngr_backend.service.Token.ValidaTokenService;
import com.taskmanager.taskmngr_backend.utils.CryptoUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    ValidaTokenService validaTokenService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if (token != null && !token.isEmpty()) {
            try {
                String decryptedToken = CryptoUtils.decrypt(token, cookieService.getSecret());

                var login = validaTokenService.validateToken(decryptedToken);

                UsuarioModel usuario = usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado",
                                "O email referenciado no token não existe na base."));

                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Não autorizado: " + ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}