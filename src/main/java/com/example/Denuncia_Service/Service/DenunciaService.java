package com.example.Denuncia_Service.Service;

import com.example.Denuncia_Service.Client.UsuarioClient;
import com.example.Denuncia_Service.Entity.Denuncia;
import com.example.Denuncia_Service.Repository.DenunciaRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;

    private final UsuarioService usuarioService;

    @Autowired
    public DenunciaService(DenunciaRepository denunciaRepository, UsuarioService usuarioService) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioService = usuarioService;
    }



    public Denuncia salvar(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    public Denuncia buscarPorId(Long idDenuncia) {
       return denunciaRepository.findById(idDenuncia).orElseThrow(()-> new RuntimeException("Denuncia não encontrada"));
    }


    public void deletarDenuncia(Long idDenuncia) {
        denunciaRepository.findById(idDenuncia);
    }

    public List<Denuncia> listarDenuncias(){
        return denunciaRepository.findAll();
    }




}
