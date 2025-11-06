package com.example.Denuncia_Service.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF. Essencial para Postman e comunicação entre microsserviços.
                .csrf(csrf -> csrf.disable())

                // 2. Define que não vamos usar sessões (STATELESS), pois usamos JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Autoriza as requisições
                .authorizeHttpRequests(authorize -> authorize
                        // 4. Permite TODAS as requisições para qualquer endpoint que comece com /denuncia/
                        // Isso impede o Spring de retornar 401 antes de chegar no seu controller.
                        .requestMatchers("/denuncia/**").permitAll()

                        // (Opcional) Você pode adicionar .anyRequest().authenticated() se tiver outros endpoints
                        // que devam ser protegidos por um filtro JWT no futuro.
                        // Por agora, vamos deixar aberto para o seu controller funcionar.
                        .anyRequest().permitAll() // Permite tudo por enquanto
                );

        return http.build();
    }
}