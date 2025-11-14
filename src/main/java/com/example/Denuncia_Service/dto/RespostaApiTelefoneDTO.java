package com.example.Denuncia_Service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RespostaApiTelefoneDTO {
    @JsonProperty("fraud_score")
    private int fraudScore;

    @JsonProperty("recent_abuse")
    private boolean recentAbuse;

    @JsonProperty("VOIP")
    private boolean voip;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("spammer")
    private boolean spammer;

}
