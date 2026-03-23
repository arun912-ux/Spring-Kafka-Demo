package com.example.springkafka.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.schema-registry-url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.producer.client-id}")
    private String producerClientId;

    // Producer configuration for Avro
    @SneakyThrows
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.PARTITIONER_ADPATIVE_PARTITIONING_ENABLE_CONFIG, true);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, org.apache.kafka.clients.producer.RoundRobinPartitioner.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put("schema.registry.url", schemaRegistryUrl);

        setAivenKafkaAuth(props);

        return props;
    }


    // Consumer configuration for Avro
    @SneakyThrows
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, org.apache.kafka.clients.consumer.StickyAssignor.class.getName());
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, org.apache.kafka.clients.consumer.CooperativeStickyAssignor.class.getName());
//        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, org.apache.kafka.clients.consumer.RoundRobinAssignor.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        setAivenKafkaAuth(props);

        return props;
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(
                producerConfigs(),
                new KafkaAvroSerializer(),
                new KafkaAvroSerializer()
        );
    }

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new KafkaAvroDeserializer(),
                new KafkaAvroDeserializer()
        );
    }

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    private void setAivenKafkaAuth(Map<String, Object> props) {
        props.put("basic.auth.credentials.source", "USER_INFO");
        props.put("basic.auth.user.info", "avnadmin:AVNS_lwo0FOA_Iy2Uu6264s4");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PEM");
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
        try {
            props.put(SslConfigs.SSL_KEYSTORE_CERTIFICATE_CHAIN_CONFIG, Files.readString(Paths.get("src/main/resources/aiven-keys/service.cert")));
            props.put(SslConfigs.SSL_KEYSTORE_KEY_CONFIG, Files.readString(Paths.get("src/main/resources/aiven-keys/service.key")));
            props.put(SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_CONFIG, Files.readString(Paths.get("src/main/resources/aiven-keys/ca.pem")));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Aiven Kafka keys. ", e);
        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        log.info("Default ACK mode: {}", factory.getContainerProperties().getAckMode());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
//        factory.setCommonErrorHandler(commonErrorHandler());
        return factory;
    }

    @Bean
    public CommonErrorHandler commonErrorHandler() {
        // BackOff strategy: e.g., initial interval 1s, max attempts 3
        FixedBackOff backOff = new FixedBackOff(10_000L, 2L);  // means 2 retries (3 attempts total)
        return new DefaultErrorHandler(deadLetterRecoverer(), backOff);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterRecoverer() {
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate(),
                        (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));
        recoverer.addNotRetryableExceptions(SerialException.class);
        return recoverer;
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<Object, byte[]> record) {
        // Process the failed message
        log.error("Received message from DLT: {}", record);
    }


}
