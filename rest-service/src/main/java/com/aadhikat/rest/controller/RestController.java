package com.aadhikat.rest.controller;

import com.aadhikat.rest.dto.FakeProducerDTO;
import com.aadhikat.rest.service.ReactiveProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("rest-service")
public class RestController {

    @Autowired
    private ReactiveProducerService service;

//    @GetMapping("rest/send/v1/{input}")
//    public Mono<Void> sendToKafkaUsingSpringKafka(@PathVariable Integer input) {
//        return service.sendViaSpringReactiveKafka(new FakeProducerDTO(input.toString()));
//    }


    @GetMapping("rest/send/v1/{input}")
    public Mono<Void> sendToKafkaUsingReactiveKafkaV1(@PathVariable Integer input) {
        return service.sendViaReactiveKafkaV1(new FakeProducerDTO(input.toString()));
    }

//    @GetMapping("rest/send/v2/{input}")
//    public Mono<Void> sendToKafkaUsingReactiveKafkaV2(@PathVariable Integer input) {
//        service.sendViaReactiveKafkaV2(new FakeProducerDTO(input.toString()));
//        return Mono.empty();
//    }


}
