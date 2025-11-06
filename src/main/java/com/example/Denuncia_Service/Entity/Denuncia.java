package com.example.Denuncia_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Denuncia")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDenuncia;
    @Column(name = "titulo_denuncia", unique = false, nullable = false)
    private String titulo;
    @Column(name = "categoria_denuncia",unique = false, nullable = false)
    private String categoria;
    @Column(name = "descricao_denuncia", unique = false, nullable = true)
    private String descricao;
    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;


}
