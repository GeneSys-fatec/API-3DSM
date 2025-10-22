package com.taskmanager.taskmngr_backend.segurancaFiltro;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.TokenInvalidoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Sempre libera preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if (token != null) {
            try {
                var login = tokenService.validateToken(token);
                UsuarioModel usuario = usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado",
                                "O email referenciado no token não existe na base."));
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (TokenInvalidoException | UsuarioNaoEncontradoException ex) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Nao autorizado: " + ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private String recoverToken(HttpServletRequest request){
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
