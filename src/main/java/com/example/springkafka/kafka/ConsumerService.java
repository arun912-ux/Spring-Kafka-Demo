package com.example.springkafka.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {


    private static final String INPUT_TOPIC = "demo-topic-1";

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Value("${kafka.producer.enabled:true}")
    private boolean producerEnabled;

    public ConsumerService() {
    }


    @RetryableTopic(
            attempts = "2",
            backOff = @BackOff(
                    delay = 10_000,
                    multiplier = 2
            ),
            dltTopicSuffix = ".dlt",
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
//    @KafkaListener(topics = {"test-topic", INPUT_TOPIC}, groupId = "spring-kafka-consumer-group-id")
//    @KafkaListener(topics = {INPUT_TOPIC}, groupId = "arbitrary-group-id")
    @KafkaListener(topics = {INPUT_TOPIC})
    public void consumeMessageWithHeaders(ConsumerRecord<Object, Object> consumerRecord) {
        if (!producerEnabled) {
            log.info("received message : {} \nwith headers : {} \nand key : {} - hashcode : {}\npartition : {}\n",
                    consumerRecord.value().toString().toUpperCase(), consumerRecord.headers(), consumerRecord.key(),
                    Utils.toPositive(Utils.murmur2(consumerRecord.key().toString().getBytes())), consumerRecord.partition());
        }
    }


    @DltHandler
    public void handleDltMessages(
            String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String dltTopic,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
            @Header(KafkaHeaders.EXCEPTION_FQCN) String exceptionClass) {

        log.error("==================================================");
        log.error("🚨 MESSAGE ROUTED TO DLT");
        log.error("DLT Topic       : {}", dltTopic);
        log.error("Payload         : {}", message);
        log.error("Error Cause     : {}", exceptionClass);
        log.error("Error Message   : {}", errorMessage);
        log.error("=================================================");
    }

}
