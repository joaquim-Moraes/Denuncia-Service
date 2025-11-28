package com.example.Denuncia_Service.Repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.Denuncia_Service.Entity.Denuncia;

@SpringBootTest
@ActiveProfiles("test")
@DataJpaTest
class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;

    private Denuncia denuncia1;
    private Denuncia denuncia2;

    @BeforeEach
    void setUp() {
        denuncia1 = new Denuncia();
        denuncia1.setTitulo("Fraude em atendimento");
        denuncia1.setCategoria("Fraude");
        denuncia1.setDescricao("Empresa cobrou valor não autorizado");
        denuncia1.setNomeEmpresa("Empresa XYZ");
        denuncia1.setIdUsuario(1L);

        denuncia2 = new Denuncia();
        denuncia2.setTitulo("Produto defeituoso");
        denuncia2.setCategoria("Defeito");
        denuncia2.setDescricao("Produto chegou com problemas");
        denuncia2.setNomeEmpresa("Empresa XYZ");
        denuncia2.setIdUsuario(2L);
    }

    @Test
    void testSalvarDenuncia() {
        // Act
        Denuncia saved = denunciaRepository.save(denuncia1);

        // Assert
        assertNotNull(saved.getIdDenuncia());
        assertEquals("Fraude em atendimento", saved.getTitulo());
        assertEquals("Empresa XYZ", saved.getNomeEmpresa());
    }

    @Test
    void testBuscarPorId() {
        // Arrange
        Denuncia saved = denunciaRepository.save(denuncia1);

        // Act
        Optional<Denuncia> found = denunciaRepository.findById(saved.getIdDenuncia());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getIdDenuncia(), found.get().getIdDenuncia());
        assertEquals("Fraude em atendimento", found.get().getTitulo());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Denuncia> found = denunciaRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByNomeEmpresa() {
        // Arrange
        denunciaRepository.save(denuncia1);
        denunciaRepository.save(denuncia2);

        // Act
        List<Denuncia> result = denunciaRepository.findByNomeEmpresa("Empresa XYZ");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getNomeEmpresa().equals("Empresa XYZ")));
    }

    @Test
    void testFindByNomeEmpresaNaoEncontrado() {
        // Arrange
        denunciaRepository.save(denuncia1);

        // Act
        List<Denuncia> result = denunciaRepository.findByNomeEmpresa("Empresa Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll() {
        // Arrange
        denunciaRepository.save(denuncia1);
        denunciaRepository.save(denuncia2);

        // Act
        List<Denuncia> result = denunciaRepository.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.size() >= 2);
    }

    @Test
    void testDeletarDenuncia() {
        // Arrange
        Denuncia saved = denunciaRepository.save(denuncia1);
        Long id = saved.getIdDenuncia();

        // Act
        denunciaRepository.deleteById(id);

        // Assert
        Optional<Denuncia> found = denunciaRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testAtualizarDenuncia() {
        // Arrange
        Denuncia saved = denunciaRepository.save(denuncia1);
        saved.setTitulo("Novo título");
        saved.setDescricao("Nova descrição");

        // Act
        Denuncia updated = denunciaRepository.save(saved);

        // Assert
        assertEquals("Novo título", updated.getTitulo());
        assertEquals("Nova descrição", updated.getDescricao());
    }

    @Test
    void testMultiplasDenunciasDoMesmoUsuario() {
        // Arrange
        Denuncia denuncia3 = new Denuncia();
        denuncia3.setTitulo("Outra denuncia");
        denuncia3.setCategoria("Categoria");
        denuncia3.setNomeEmpresa("Outra Empresa");
        denuncia3.setIdUsuario(1L);

        denunciaRepository.save(denuncia1);
        denunciaRepository.save(denuncia3);

        // Act
        List<Denuncia> result = denunciaRepository.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(2, result.stream().filter(d -> d.getIdUsuario().equals(1L)).count());
    }
}
