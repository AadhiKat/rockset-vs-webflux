package com.aadhikat.rsocket.service;

import com.aadhikat.rsocket.config.KafkaProducerFactory;
import com.aadhikat.rsocket.dto.FakeProducerDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;

@Service
public class ReactiveProducerService {

    private final Logger log = LoggerFactory.getLogger(ReactiveProducerService.class);
    private final KafkaProducerFactory producerFactory;

    @Value(value = "${FAKE_PRODUCER_DTO_TOPIC}")
    private String topic;

    public ReactiveProducerService(KafkaProducerFactory producerFactory) {
        this.producerFactory = producerFactory;
    }

    public Mono<Void> send(FakeProducerDTO fakeProducerDTO) {
        return producerFactory.getKafkaSenderMono()
                .flatMap(kafkaSender ->
                        kafkaSender
                                .createOutbound()
                                .send(Mono.just(new ProducerRecord<>(topic, fakeProducerDTO.toString())))
                                .then()
                                .doOnSuccess(senderResult -> log.info("sent producer : " + fakeProducerDTO))
                );
    }


    // V1 implementation
    // Writing this way results in small portion of records being written into Kafka

    public Mono<Void> sendMono(Mono<FakeProducerDTO> fakeProducerDTO) {
        return producerFactory.getKafkaSenderMono()
                .map(kafkaSender ->
                        kafkaSender
                                .createOutbound()
                                .send(fakeProducerDTO.map(dto -> new ProducerRecord<>(topic, dto.toString())))
                                .then()
                                .doOnError(e -> log.error("Error", e))
                                .subscribe() //https://projectreactor.io/docs/kafka/milestone/api/reactor/kafka/sender/KafkaSender.html
                )
                .then();
    }


//    public void sendV2(FakeProducerDTO fakeProducerDTO) {
//        producerFactory.getKafkaSenderMono()
//                .map(kafkaSender ->
//                        kafkaSender
//                                .createOutbound()
//                                .send(Mono.just(new ProducerRecord<>(topic, fakeProducerDTO.toString())))
//                                .then()
//                                .subscribe()
//                )
//                .doOnError(e -> {
//                    log.error("Send failed: ", e);
//                })
//                .doOnSuccess(r ->
//                        log.info("Sent Successfully All message: {}", r));
//    }
}
