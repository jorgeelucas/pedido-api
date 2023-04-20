package com.pedidoapi.client;

import com.pedidoapi.client.dto.ProdutoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProdutoClient {

    private static final String PRODUTOS_API_URL = "http://localhost:8080";

    private final WebClient client;

    public ProdutoClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(PRODUTOS_API_URL).build();
    }

    public Mono<ProdutoResponse> buscarProdutoPorId(String idProduto) {
        return client
                .get()
                .uri("/produtos/" + idProduto)
                .exchangeToMono(result -> {
                    if (result.statusCode().is2xxSuccessful()) {
                        return result.bodyToMono(ProdutoResponse.class)
                                .switchIfEmpty(Mono.error(new RuntimeException("Produto inexistente!")));
                    } else {
                        return Mono.error(new RuntimeException("Erro na chamada"));
                    }
                });
    }
}
