package com.pedidoapi.client.dto;

import java.math.BigDecimal;

public record ProdutoResponse (String id, String nome, BigDecimal preco, int qtd) {
}
