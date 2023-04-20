package com.pedidoapi.dto;

import java.util.List;

public record PedidoRequest(
        List<ItemDTO> itens
) {
}
