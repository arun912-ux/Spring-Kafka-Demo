package com.example.springkafka.config;

import com.example.springkafka.avro.UserEvent;
import com.example.springkafka.avro.UserKey;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    @Value("${spring.kafka.streams.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Bean(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Set default serdes if you like
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
        // Schema Registry config
        props.put("schema.registry.url", schemaRegistryUrl);
        // For specific Avro reader:
        props.put("specific.avro.reader", true);

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public Serde<UserKey> userKeySerde() {
        SpecificAvroSerde<UserKey> serde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = Map.of(
                "schema.registry.url", schemaRegistryUrl
        );
        serde.configure(serdeConfig, true);
        return serde;
    }
    @Bean
    public Serde<UserEvent> userEventSerde() {
        SpecificAvroSerde<UserEvent> serde = new SpecificAvroSerde<>();
        Map<String, String> serdeConfig = Map.of(
                "schema.registry.url", schemaRegistryUrl
        );
        serde.configure(serdeConfig, false);
        return serde;
    }

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(KafkaStreamsConfiguration kStreamsConfig) {
        return new StreamsBuilderFactoryBean(kStreamsConfig());
    }


}
