package com.pedidoapi.jobs;

import com.pedidoapi.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RequiredArgsConstructor
public class ConcluirPedidoJob implements InitializingBean {

    private final PedidoService pedidoService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        pedidoService.buscarPorId("id")
                .subscribe();

        Mono.fromRunnable(this::nada).subscribe();

    }

    public void nada() {
        System.out.println("nada");
    }
}
