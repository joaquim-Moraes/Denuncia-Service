package com.example.Denuncia_Service.Repository;

import com.example.Denuncia_Service.Entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    List<Denuncia> findByCategoria(String categoria);
    List<Denuncia> findByTitulo(String titulo);
    Optional<Denuncia> findById(Long idDenuncia);

}
