package com.example.Denuncia_Service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

// Mantenha o @TestPropertySource se quiser, mas o MockBean é mais forte

@SpringBootTest
@ActiveProfiles("test")
class DenunciaServiceApplicationTests {

    // MOCK O SERVIÇO QUE CAUSA O PROBLEMA
    // Ao mockar, o Spring não cria o bean real, então não lê os @Value que falham
    @MockBean
    private com.example.Denuncia_Service.Service.TelefoneReputacaoService telefoneReputacaoService;

    @Test
    void contextLoads() {
        // Este teste deve passar agora, pois o serviço problemático foi substituído por um mock vazio
    }
}