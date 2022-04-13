package com.aadhikat.rsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Controller
public class RSocketController {

    @MessageMapping("rr.square")
    public Mono<Integer> findSquare(Mono<Integer> integerMono){
        return integerMono.delayElement(Duration.of(2, ChronoUnit.MILLIS));
    }

    @MessageMapping("rc.square")
    public Flux<Integer> findSquare(Flux<Integer> integerFlux){
        return integerFlux.window(10).delayElements(Duration.of(2, ChronoUnit.MILLIS)).flatMap(d -> d);
    }

}
