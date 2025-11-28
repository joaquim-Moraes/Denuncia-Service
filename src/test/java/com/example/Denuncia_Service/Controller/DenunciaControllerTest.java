package com.example.Denuncia_Service.Controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Denuncia_Service.Entity.Denuncia;
import com.example.Denuncia_Service.Security.JwtUtil;
import com.example.Denuncia_Service.Service.DenunciaService;
import com.example.Denuncia_Service.Service.TelefoneReputacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DenunciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DenunciaService denunciaService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TelefoneReputacaoService telefoneReputacaoService;

    private Denuncia denuncia;

    @BeforeEach
    void setUp() {
        denuncia = new Denuncia();
        denuncia.setIdDenuncia(1L);
        denuncia.setTitulo("Fraude em atendimento");
        denuncia.setCategoria("Fraude");
        denuncia.setDescricao("Empresa cobrou valor não autorizado");
        denuncia.setNomeEmpresa("Empresa XYZ");
        denuncia.setIdUsuario(1L);
    }

    @Test
    void testCriarDenunciaComSucesso() throws Exception {
        // Arrange
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(1L);
        when(denunciaService.salvar(any(Denuncia.class))).thenReturn(denuncia);

        // Act & Assert
        mockMvc.perform(post("/denuncia/cadastrar")
                .header("Authorization", "Bearer token-valido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denuncia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDenuncia", is(1)))
                .andExpect(jsonPath("$.titulo", is("Fraude em atendimento")))
                .andExpect(jsonPath("$.nomeEmpresa", is("Empresa XYZ")));

        verify(jwtUtil, times(1)).getUserIdFromToken(anyString());
        verify(denunciaService, times(1)).salvar(any(Denuncia.class));
    }

    @Test
    void testFiltrarPorIdComSucesso() throws Exception {
        // Arrange
        when(denunciaService.buscarPorId(1L)).thenReturn(denuncia);

        // Act & Assert
        mockMvc.perform(get("/denuncia/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDenuncia", is(1)))
                .andExpect(jsonPath("$.titulo", is("Fraude em atendimento")));

        verify(denunciaService, times(1)).buscarPorId(1L);
    }

    @Test
    void testFiltrarPorIdNaoEncontrado() throws Exception {
        // Arrange
        when(denunciaService.buscarPorId(999L))
                .thenThrow(new RuntimeException("Denuncia não encontrada"));

        // Act & Assert (Modificado)
        jakarta.servlet.ServletException exception = org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(get("/denuncia/999")
                    .contentType(MediaType.APPLICATION_JSON));
        });

        // Opcional
        org.junit.jupiter.api.Assertions.assertEquals("Denuncia não encontrada", exception.getCause().getMessage());
    }

    @Test
    void testListarDenunciasComSucesso() throws Exception {
        // Arrange
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setIdDenuncia(2L);
        denuncia2.setTitulo("Outra denuncia");
        denuncia2.setNomeEmpresa("Empresa ABC");

        List<Denuncia> denuncias = Arrays.asList(denuncia, denuncia2);
        when(denunciaService.listarDenuncias()).thenReturn(denuncias);

        // Act & Assert
        mockMvc.perform(get("/denuncia/listar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idDenuncia", is(1)))
                .andExpect(jsonPath("$[1].idDenuncia", is(2)));

        verify(denunciaService, times(1)).listarDenuncias();
    }

    @Test
    void testListarDenunciasVazio() throws Exception {
        // Arrange
        when(denunciaService.listarDenuncias()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/denuncia/listar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(denunciaService, times(1)).listarDenuncias();
    }

    @Test
    void testDeletarDenunciaComSucesso() throws Exception {
        // Arrange
        doNothing().when(denunciaService).deletarDenuncia(1L);

        // Act & Assert
        mockMvc.perform(delete("/denuncia/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(denunciaService, times(1)).deletarDenuncia(1L);
    }

@Test
    void testCriarDenunciaTokenInvalido() throws Exception {
        // Arrange
        when(jwtUtil.getUserIdFromToken(anyString()))
                .thenThrow(new RuntimeException("Token inválido"));

        // Act & Assert (Modificado para esperar a Exceção)
        // Usamos assertThrows porque o Controller não tem try-catch/ExceptionHandler
        jakarta.servlet.ServletException exception = org.junit.jupiter.api.Assertions.assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(post("/denuncia/cadastrar")
                    .header("Authorization", "Bearer token-invalido")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(denuncia)));
        });
        
        // Opcional: Verificar se a causa raiz é a RuntimeException do Token
        org.junit.jupiter.api.Assertions.assertTrue(exception.getCause() instanceof RuntimeException);
        org.junit.jupiter.api.Assertions.assertEquals("Token inválido", exception.getCause().getMessage());
    }

    @Test
    void testCriarDenunciaSemToken() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/denuncia/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(denuncia)))
                .andExpect(status().isBadRequest());
    }
}
