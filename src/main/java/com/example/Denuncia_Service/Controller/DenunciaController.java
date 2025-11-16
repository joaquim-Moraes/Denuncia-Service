package com.example.Denuncia_Service.Controller;

import com.example.Denuncia_Service.Entity.Denuncia;
import com.example.Denuncia_Service.Security.JwtUtil;
import com.example.Denuncia_Service.Service.DenunciaService;
import com.example.Denuncia_Service.Service.TelefoneReputacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/denuncia")
public class DenunciaController {
    @Autowired
    private DenunciaService denunciaService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TelefoneReputacaoService telefoneReputacaoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Denuncia> criarDenuncia(@RequestBody Denuncia denuncia,
                                                  @RequestHeader("Authorization") String token) {
        Long idUsuario = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));

        denuncia.setIdUsuario(idUsuario);

        Denuncia saved = denunciaService.salvar(denuncia);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{idDenuncia}")
    public Denuncia filtrarPorId(@PathVariable Long idDenuncia) {
        return denunciaService.buscarPorId(idDenuncia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDenuncia(@PathVariable Long id
                                               ) {
        denunciaService.deletarDenuncia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Denuncia>> listarDenuncias() {
        List<Denuncia> denuncias = denunciaService.listarDenuncias();
        return ResponseEntity.ok(denuncias);
    }

    @GetMapping("/verificar-reputacao")
    public ResponseEntity<?> verificarReputacao(
            @RequestParam String telefone, // <-- AQUI! O usuário digita este número.
            @RequestHeader("Authorization") String token // <-- AQUI! O usuário logado.
    ) {

        jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));

        // 2. O 'telefone' (que o usuário digitou) é enviado para a API externa
        Object reputacao = telefoneReputacaoService.buscarReputacao(telefone);

        return ResponseEntity.ok(reputacao);
    }

}
