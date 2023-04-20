package com.pedidoapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemDTO(
        @JsonProperty("produto")
        String idProduto,
        int quantidade
) {
}
