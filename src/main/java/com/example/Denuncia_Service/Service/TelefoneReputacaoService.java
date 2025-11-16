package com.example.Denuncia_Service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Denuncia_Service.dto.RespostaApiTelefoneDTO;

@Service
public class TelefoneReputacaoService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${IPQS_KEY}")
    private String telefoneApiKey;
    @Value("${IPQS_URL}")
    private String apiUrl;


    public RespostaApiTelefoneDTO buscarReputacao(String numero) {

        String urlCompleta = apiUrl + "/" + telefoneApiKey + "/+55" + numero;

        try {
            // 3. PEDIMOS AO RESTTEMPLATE PARA USAR A CLASSE DTO
            RespostaApiTelefoneDTO resposta = restTemplate.getForObject(
                    urlCompleta,
                    RespostaApiTelefoneDTO.class // <-- MUDANÇA PRINCIPAL (não é Map.class)
            );

            return resposta;

        } catch (Exception e) {
            System.err.println("Erro ao chamar API de telefone: " + e.getMessage());
            throw new RuntimeException("Falha ao consultar serviço de reputação.", e);
        }
    }


}
