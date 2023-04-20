package com.pedidoapi.dto;

import com.pedidoapi.entity.Pedido;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(String id,
                             List<ItemDTO> itens,
                             BigDecimal total,
                             Pedido.Status status) {
}
