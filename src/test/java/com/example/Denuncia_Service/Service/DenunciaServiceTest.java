package com.example.Denuncia_Service.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Denuncia_Service.Entity.Denuncia;
import com.example.Denuncia_Service.Repository.DenunciaRepository;

@ExtendWith(MockitoExtension.class)
class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaService denunciaService;

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
    void testSalvarDenuncia() {
        // Arrange
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denuncia);

        // Act
        Denuncia result = denunciaService.salvar(denuncia);

        // Assert
        assertNotNull(result);
        assertEquals("Fraude em atendimento", result.getTitulo());
        assertEquals("Fraude", result.getCategoria());
        verify(denunciaRepository, times(1)).save(denuncia);
    }

    @Test
    void testBuscarPorIdComSucesso() {
        // Arrange
        when(denunciaRepository.findById(1L)).thenReturn(Optional.of(denuncia));

        // Act
        Denuncia result = denunciaService.buscarPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdDenuncia());
        assertEquals("Fraude em atendimento", result.getTitulo());
        verify(denunciaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Arrange
        when(denunciaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            denunciaService.buscarPorId(999L);
        });

        assertEquals("Denuncia não encontrada", exception.getMessage());
        verify(denunciaRepository, times(1)).findById(999L);
    }

    @Test
    void testBuscarPorNomeEmpresa() {
        // Arrange
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setIdDenuncia(2L);
        denuncia2.setNomeEmpresa("Empresa XYZ");
        denuncia2.setTitulo("Outro problema");

        List<Denuncia> denuncias = Arrays.asList(denuncia, denuncia2);
        when(denunciaRepository.findByNomeEmpresa("Empresa XYZ")).thenReturn(denuncias);

        // Act
        List<Denuncia> result = denunciaService.buscarPorNome("Empresa XYZ");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getNomeEmpresa().equals("Empresa XYZ")));
        verify(denunciaRepository, times(1)).findByNomeEmpresa("Empresa XYZ");
    }

    @Test
    void testListarDenuncias() {
        // Arrange
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setIdDenuncia(2L);
        denuncia2.setTitulo("Outra denuncia");

        List<Denuncia> denuncias = Arrays.asList(denuncia, denuncia2);
        when(denunciaRepository.findAll()).thenReturn(denuncias);

        // Act
        List<Denuncia> result = denunciaService.listarDenuncias();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(denunciaRepository, times(1)).findAll();
    }

    @Test
    void testListarDenunciasVazio() {
        // Arrange
        when(denunciaRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Denuncia> result = denunciaService.listarDenuncias();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(denunciaRepository, times(1)).findAll();
    }

    @Test
    void testDeletarDenuncia() {
        // Arrange
        when(denunciaRepository.findById(1L)).thenReturn(Optional.of(denuncia));

        // Act
        denunciaService.deletarDenuncia(1L);

        // Assert
        verify(denunciaRepository, times(1)).findById(1L);
    }

    @Test
    void testSalvarDenunciaComValoresInvalidos() {
        // Arrange
        Denuncia denunciaInvalida = new Denuncia();
        denunciaInvalida.setTitulo(null);
        denunciaInvalida.setCategoria(null);

        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denunciaInvalida);

        // Act
        Denuncia result = denunciaService.salvar(denunciaInvalida);

        // Assert
        assertNotNull(result);
        assertNull(result.getTitulo());
        verify(denunciaRepository, times(1)).save(denunciaInvalida);
    }
}
