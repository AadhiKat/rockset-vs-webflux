package com.aadhikat.controller;

import com.aadhikat.service.RSocketService;
import com.aadhikat.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AggregateController {

    @Autowired
    private RSocketService rSocketService;

    @Autowired
    private RestService restService;

    @GetMapping("rest/send/v1/{input}")
    public Mono<Void> restSquareV1(@PathVariable int input) {
        return this.restService.requestResponseV1(input);
    }

//    @GetMapping("rest/send/v2/{input}")
//    public Mono<Void> restSquareV2(@PathVariable int input) {
//        return this.restService.requestResponseV2(input);
//    }

    @GetMapping("rsocket/square/{input}")
    public Mono<Void> rsocketSquareFF(@PathVariable int input) {
        return this.rSocketService.fireAndForget(input);
    }

    @GetMapping("rsocket/send/v1/{input}")
    public Mono<Void> rsocketSendFFV1(@PathVariable int input) {
        return this.rSocketService.fireAndForgetMonoV1(input);
    }

//    @GetMapping("rsocket/send/v2/{input}")
//    public Mono<Void> rsocketSendFFV2(@PathVariable int input) {
//        return this.rSocketService.fireAndForgetMonoV2(input);
//    }

//    @GetMapping("rsocket/rr/square/{input}")
//    public Mono<Map<Integer, Integer>> rsocketSquareRR(@PathVariable int input){
//        return this.rSocketService.requestResponse(input);
//    }

//    @GetMapping("rsocket/rc/square/{input}")
//    public Mono<Void> rsocketSquareRC(@PathVariable int input){
//        return this.rSocketService.requestChannel(input);
//    }
}
