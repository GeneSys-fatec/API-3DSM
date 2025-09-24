package com.taskmanager.taskmngr_backend.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isPublicRoute(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request); // Busca o Token.
        if (token != null) {
            var login = tokenService.validateToken(token); // Valida o Token. 
            if (login != null) {
                UsuarioModel usuario = usuarioRepository.findByEmail(login) // Carrega o usuário do banco e registra. 
                        .orElseThrow(() -> new RuntimeException("Usuário referenciado no token não foi encontrado."));
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Pega o Token do header.
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    // Rotas públicas que não precisam de autenticação.
    private boolean isPublicRoute(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        var publicRoutes = List.of(
                contextPath + "/auth/login",
                contextPath + "/auth/cadastrar"
        );
        return publicRoutes.stream().anyMatch(route -> request.getRequestURI().equals(route));
    }
}
