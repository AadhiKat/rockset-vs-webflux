package com.aadhikat.rsocket.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@Getter
public class KafkaProducerFactory {

    @Value("${bootstrap-servers}")
    private String bootstrapServers;

    @Value("${sandbox-cluster-username}")
    private String sandboxClusterUsername;

    @Value("${sandbox-cluster-password}")
    private String sandboxClusterPassword;

    private final String securityProtocol = "SASL_SSL";
    private final String securitySaslMechanism = "PLAIN";

    private final int maxInFlightMessages = 65536;


    private SenderOptions getSenderOptions() {

        String saslJaasConfig = "org.apache.kafka.common.security.plain.PlainLoginModule required\n" +
                "    username=\"" + sandboxClusterUsername + "\"\n" +
                "    password=\"" + sandboxClusterPassword + "\";";

        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 65536);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5242880);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, securitySaslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);

        return SenderOptions.create(props)
//                .maxInFlight(maxInFlightMessages)
                .stopOnError(false);
    }

    private SenderOptions getLocalSenderOptions() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 13554432);
        producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 65536);
        producerProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        producerProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        producerProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 5242880);
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);

        return SenderOptions.create(producerProps)
                .maxInFlight(maxInFlightMessages)
                .stopOnError(false);
    }

    KafkaSender<String, String> sender ;


    public Mono<KafkaSender<String, String>> getKafkaSenderMono() {
        try {
            if(sender == null)
                sender = KafkaSender.create(getSenderOptions());

            return Mono.just(sender);
        } catch (Exception e) {
            log.error("Kafka create failed");
            return Mono.error(e);
        }
    }
}


