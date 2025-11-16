package com.example.Denuncia_Service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Denuncia_Service.Entity.Denuncia;
import com.example.Denuncia_Service.Repository.DenunciaRepository;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;


    @Autowired
    public DenunciaService(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    
    }



    public Denuncia salvar(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    public Denuncia buscarPorId(Long idDenuncia) {
       return denunciaRepository.findById(idDenuncia).orElseThrow(()-> new RuntimeException("Denuncia não encontrada"));
    }
    public List<Denuncia> buscarPorNome(String nomeEmpresa){
        return denunciaRepository.findByNomeEmpresa(nomeEmpresa);
    }
    

    public void deletarDenuncia(Long idDenuncia) {
        denunciaRepository.findById(idDenuncia);
    }

    public List<Denuncia> listarDenuncias(){
        return denunciaRepository.findAll();
    }




}
