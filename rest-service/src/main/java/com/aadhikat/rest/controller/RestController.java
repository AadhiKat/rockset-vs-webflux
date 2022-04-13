package com.aadhikat.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("rest-service")
public class RestController {

    @GetMapping("rest/square/{input}")
    public Mono<Integer> findSquare(@PathVariable Integer input) {
        return Mono.just(input).delayElement(Duration.of(2, ChronoUnit.MILLIS));
    }
}
