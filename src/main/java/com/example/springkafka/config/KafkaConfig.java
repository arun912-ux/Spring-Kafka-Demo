//package com.example.springkafka.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
//
//@Configuration
//public class KafkaConfig {
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(getProducerConfig());
//    }
//
//
//    /**
//     * Returns the producer configuration settings for Kafka.
//     *
//     * @return The producer configuration settings.
//     */
//    public Map<String, Object> getProducerConfig() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(BOOTSTRAP_SERVERS_CONFIG, "192.168.1.71:9092");
////        config.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
////        config.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        config.put(ACKS_CONFIG, "all");
//        config.put(RETRIES_CONFIG, "1");
//        config.put(KEY_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer");
//        config.put(VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer");
//        config.put(BATCH_SIZE_CONFIG, 16384);
//        config.put(BUFFER_MEMORY_CONFIG, 33554432);
//        config.put(LINGER_MS_CONFIG, 100);
//        config.put("max.in.flight.messages", 1000000);
//        config.put("max.request.size", 1048576);
//        config.put("receive.buffer.bytes", 262144);
//        config.put("send.buffer.bytes", 262144);
//        config.put("timeout.ms", 30000);
//        config.put("metadata.fetch.timeout.ms", 60000);
//        config.put("reconnect.backoff.ms", 50);
//        config.put("reconnect.backoff.max.ms", 1000);
//        config.put("retry.backoff.ms", 100);
//        config.put("retry.backoff.max.ms", 1000);
//        config.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");
//        return config;
//    }
//
//}
