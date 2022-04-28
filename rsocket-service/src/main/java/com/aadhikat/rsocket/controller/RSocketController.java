package com.aadhikat.rsocket.controller;

import com.aadhikat.rsocket.dto.FakeProducerDTO;
import com.aadhikat.rsocket.service.ReactiveProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Controller
public class RSocketController {

    @Autowired
    private ReactiveProducerService service;

    @MessageMapping("ff.square.{input}")
    public Mono<Void> findSquare(@DestinationVariable int input) {
        return service.send(new FakeProducerDTO(String.valueOf(input)));
    }

    // V1 implementation
    // Implementing this way results in half of the records being written to Kafka

    @MessageMapping("ff.send.v1")
    public Mono<Void> findSend(Mono<Integer> integerMono) {
        return service
                .sendMono(
                        integerMono
                                .map(integer -> new FakeProducerDTO(String.valueOf(integer)))
                );
    }


//    @MessageMapping("ff.send.v2")
//    public Mono<Void> sendV2(Mono<Integer> integerMono) {
//        integerMono
//                .subscribe(integer ->
//                        service.sendV2(
//                                new FakeProducerDTO(String.valueOf(integer))
//                        )
//                );
//        return Mono.empty();
//    }


//    @MessageMapping("ff.send.v3")
//    public Mono<Void> sendV3(Mono<Integer> integerMono, RSocketRequester requester) {
//        service.sendV3(integerMono.map(i -> new FakeProducerDTO(i.toString())) , requester);
//        return Mono.empty();
//    }


    @MessageMapping("rr.square")
    public Mono<Integer> findSquare(Mono<Integer> integerMono) {
        return integerMono.delayElement(Duration.of(2, ChronoUnit.MILLIS));
    }

}
