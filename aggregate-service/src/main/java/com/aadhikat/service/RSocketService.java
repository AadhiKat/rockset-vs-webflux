package com.aadhikat.service;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class RSocketService {

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester requester;

    @Value("${rsocket.service.host}")
    private String host;

    @Value("${rsocket.service.port}")
    private int port;

    @PostConstruct
    private void init() {
        this.requester = this.builder
                .rsocketConnector(c -> c
                        .reconnect(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(15))
                                .doBeforeRetry(s -> System.out.println("Retrying : " + s.totalRetriesInARow()))
                        ))
                .transport(TcpClientTransport.create(host, port));
    }


    public Mono<Void> fireAndForget(int input) {
        return Flux.range(1, input)
                .flatMap(i -> this.requester
                        .route("ff.square." + i)
                        .send())
                .then();
    }

    public Mono<Void> fireAndForgetMonoV1(int input) {
        return Flux.range(1, input)
                .doOnNext(System.out::println)
                .flatMap(i -> this.requester
                        .route("ff.send.v1")
                        .data(i)
                        .send()
                )
                .then();
    }

//    public Mono<Void> fireAndForgetMonoV2(int input) {
//        return Flux.range(1, input)
//                .doOnNext(System.out::println)
//                .map(i -> this.requester
//                        .route("ff.send.v2")
//                        .data(i)
//                        .send()
//                )
//                .then();
//    }

//    public Mono<Map<Integer, Integer>> requestResponse(int input) {
//        return Flux.range(1, input)
//                .flatMap(i -> this.requester.route("rr.square")
//                        .data(i)
//                        .retrieveMono(Integer.class)
//                        .map(k -> Tuples.of(i, k))
//                )
//                .collectMap(Tuple2::getT1, Tuple2::getT2);
//    }

//    public Mono<Map<Integer, Integer>> requestChannel(int input) {
//        AtomicInteger atomicInteger = new AtomicInteger(1);
//        return this.requester.route("rc.square")
//                .data(Flux.range(1, input))
//                .retrieveFlux(Integer.class)
//                .collectMap(i -> atomicInteger.getAndIncrement(), i -> i);
//    }

}
