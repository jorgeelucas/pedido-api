package com.pedidoapi.service;

import com.pedidoapi.client.ProdutoClient;
import com.pedidoapi.client.dto.ProdutoResponse;
import com.pedidoapi.dto.ItemDTO;
import com.pedidoapi.dto.PedidoRequest;
import com.pedidoapi.dto.PedidoResponse;
import com.pedidoapi.entity.Pedido;
import com.pedidoapi.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ProdutoClient produtoClient;

    public Mono<PedidoResponse> salvar(PedidoRequest pedidoDTO) {

        String uid = UUID.randomUUID().toString();

        var itensEntity = pedidoDTO.itens().stream()
                .map(dto -> new Pedido.Item(dto.idProduto(), dto.quantidade()))
                .toList();

        return calcularPrecoProdutos(pedidoDTO)
                .map(preco -> {
                    var pedidoEntity = new Pedido(uid,
                            itensEntity,
                            LocalDateTime.now(),
                            Pedido.Status.REALIZADO,
                            preco);

                    repository.salvar(pedidoEntity);

                    return pedidoEntity;
                }).map(entity -> new PedidoResponse(uid, pedidoDTO.itens(), entity.total(), Pedido.Status.REALIZADO));
    }

    private Mono<BigDecimal> calcularPrecoProdutos(PedidoRequest pedidoDTO) {

        List<ItemDTO> itens = pedidoDTO.itens();

        return Flux.fromIterable(itens)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(item ->
                        produtoClient.buscarProdutoPorId(item.idProduto())
                                .filter(produto -> produto.qtd() >= item.quantidade())
                                .switchIfEmpty(Mono.error(new RuntimeException("Quantidade insuficiente: " + item.idProduto())))
                                .map(produtoResponse -> {
                                    BigDecimal qtdBD = new BigDecimal(item.quantidade());
                                    return produtoResponse.preco().multiply(qtdBD);
                                }))
                .reduce(BigDecimal::add);
    }

    public Mono<PedidoResponse> buscarPorId(String id) {
        return Mono.defer(() -> Mono.justOrEmpty(repository.buscarPorId(id)))
                .subscribeOn(Schedulers.boundedElastic())
                .map(entidade -> {
                    var itensDTO = entidade.itens().stream()
                            .map(it -> new ItemDTO(it.idProduto(), it.quantidade()))
                            .toList();

                    return new PedidoResponse(entidade.id(), itensDTO, entidade.total(), entidade.status());
                });
    }

}
