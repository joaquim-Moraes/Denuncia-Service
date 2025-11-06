package com.example.Denuncia_Service.Service;

import com.example.Denuncia_Service.Client.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {


    @Autowired
    private RestTemplate restTemplate;
    @Value("${user-service.api.url}")
    private String userServiceUrl;

    public UsuarioClient buscarUsuarioPorId(int idUsuario) {
        String url = userServiceUrl + "/usuario/buscar?id=" + idUsuario;

        try {
            // 2. Faz a chamada GET e espera o JSON ser convertido para UsuarioClient
            UsuarioClient usuario = restTemplate.getForObject(url, UsuarioClient.class);
            return usuario;

        } catch (HttpClientErrorException.NotFound e) {
            // 3. O user-service disse que o usuário não existe (Erro 404)
            throw new RuntimeException("Usuário com ID " + idUsuario + " não encontrado no user-service.", e);
        } catch (Exception e) {
            // 4. Outro erro (ex: user-service está fora do ar)
            throw new RuntimeException("Erro ao se comunicar com o user-service: " + e.getMessage(), e);
        }
    }
}

