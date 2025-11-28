package com.example.Denuncia_Service.Security;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String secret;
    private Key key;
    private String validToken;

    @BeforeEach
    void setUp() {
        // Secret com 256 bits (32 caracteres em base64)
        secret = "F7+tY9gKx5n8tVq/eWBJmB+P6b8G0i/tUa1rZc3xO2L/n2dD5e+rA8dG9jH4kL7fJ6gKq0mN4oP1qQ==";
        key = Keys.hmacShaKeyFor(secret.getBytes());

        // Injetar o secret via reflection
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        
        // Chamar init para inicializar a chave
        jwtUtil.init();

        // Criar um token válido com ID 1
        validToken = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(key)
                .compact();
    }

    @Test
    void testGetUserIdFromTokenComSucesso() {
        // Act
        Long userId = jwtUtil.getUserIdFromToken(validToken);

        // Assert
        assertNotNull(userId);
        assertEquals(1L, userId);
    }

    @Test
    void testGetUserIdFromTokenComIdDiferente() {
        // Arrange
        String tokenWithId42 = Jwts.builder()
                .setSubject("42")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        // Act
        Long userId = jwtUtil.getUserIdFromToken(tokenWithId42);

        // Assert
        assertEquals(42L, userId);
    }

    @Test
    void testGetUserIdFromTokenExpirado() {
        // Arrange
        String expiredToken = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // Expirado há 1 hora
                .signWith(key)
                .compact();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jwtUtil.getUserIdFromToken(expiredToken);
        });
    }

    @Test
    void testGetUserIdFromTokenInvalido() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jwtUtil.getUserIdFromToken("token.invalido.aqui");
        });
    }

    @Test
    void testGetUserIdFromTokenComSignatureInvalida() {
        // Arrange - Criar um token com chave diferente
        // Uma string bem longa e segura para o teste
        Key outraChave = Keys.hmacShaKeyFor("umaSenhaFalsaQueSejaRealmenteMuitoLongaEInseguraParaTeste123".getBytes());
        String tokenComOutraChave = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(outraChave)
                .compact();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jwtUtil.getUserIdFromToken(tokenComOutraChave);
        });
    }

    @Test
    void testGetUserIdFromTokenSubjectNulo() {
        // Arrange
        String tokenSemSubject = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jwtUtil.getUserIdFromToken(tokenSemSubject);
        });
    }

    @Test
    void testGetAllClaimsComSucesso() {
        // Act
        var claims = jwtUtil.getAllClaims(validToken);

        // Assert
        assertNotNull(claims);
        assertEquals("1", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void testJwtUtilInit() {
        // Verificar que o init foi chamado e a chave foi inicializada
        assertNotNull(ReflectionTestUtils.getField(jwtUtil, "key"));
    }

    @Test
    void testGetUserIdFromTokenComIdGrande() {
        // Arrange
        String tokenComIdGrande = Jwts.builder()
                .setSubject("999999999")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        // Act
        Long userId = jwtUtil.getUserIdFromToken(tokenComIdGrande);

        // Assert
        assertEquals(999999999L, userId);
    }
}
