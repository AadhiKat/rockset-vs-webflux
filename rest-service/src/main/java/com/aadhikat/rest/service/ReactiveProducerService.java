package com.aadhikat.rest.service;

import com.aadhikat.rest.config.KafkaProducerFactory;
import com.aadhikat.rest.dto.FakeProducerDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

@Service
public class ReactiveProducerService {

    private final Logger log = LoggerFactory.getLogger(ReactiveProducerService.class);
//    private final ReactiveKafkaProducerTemplate<String, FakeProducerDTO> reactiveKafkaProducerTemplate;
    private KafkaProducerFactory producerFactory;


    @Value(value = "${FAKE_PRODUCER_DTO_TOPIC}")
    private String topic;

//    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, FakeProducerDTO> reactiveKafkaProducerTemplate, KafkaProducerFactory producerFactory) {
//        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
//        this.producerFactory = producerFactory;
//    }

    public ReactiveProducerService(KafkaProducerFactory producerFactory) {
        this.producerFactory = producerFactory;
    }

//    public Mono<Void> sendViaSpringReactiveKafka(FakeProducerDTO fakeProducerDTO) {
//        log.info("send to topic={}, {}={},", topic, FakeProducerDTO.class.getSimpleName(), fakeProducerDTO);
//        return reactiveKafkaProducerTemplate.send(topic, fakeProducerDTO)
//                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", fakeProducerDTO, senderResult.recordMetadata().offset()))
//                .then();
//    }

    public Mono<Void> sendViaReactiveKafkaV1(FakeProducerDTO fakeProducerDTO) {
        Mono<KafkaSender<String, String>> kafkaSenderMono = producerFactory.getKafkaSender();
        return kafkaSenderMono
                .map(kafkaSender ->
                        kafkaSender
                                .createOutbound()
                                .send(Mono.just(new ProducerRecord<>(topic, fakeProducerDTO.toString())))
                                .then()
                                .doOnNext(senderResult -> log.info("sent producer : " + fakeProducerDTO))
                                .doOnError(System.out::println)
                                .subscribe()
                ).then();
    }

//    public void sendViaReactiveKafkaV2(FakeProducerDTO fakeProducerDTO) {
//        Mono<KafkaSender<String, String>> kafkaSenderMono = producerFactory.getKafkaSender();
//        kafkaSenderMono
//                .flatMap(kafkaSender ->
//                        kafkaSender
//                                .createOutbound()
//                                .send(Mono.just(new ProducerRecord<>(topic, fakeProducerDTO.toString())))
//                                .then()
//                                .doOnSuccess(senderResult -> log.info("sent producer : " + fakeProducerDTO))
//
//                )
//                .subscribe(System.out::println);
//    }

}
