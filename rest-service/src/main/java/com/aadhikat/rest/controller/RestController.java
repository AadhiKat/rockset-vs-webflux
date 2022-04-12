package com.aadhikat.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("rest-service")
public class RestController {

    @GetMapping("rest/square/{input}")
    public Mono<Integer> findSquare(@PathVariable int input) {
        return Mono.just(input * input);
    }
}
