package com.example.springkafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsumerService {


    private final ObjectMapper mapper;

//    public ConsumerService(ObjectMapper mapper) {
//        this.mapper = mapper;
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//    }

    private static final String INPUT_TOPIC = "input-topic";

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(
                    delay = 2000,
                    multiplier = 2
            ),
            dltTopicSuffix = "--custom-dlt"
    )
    @KafkaListener(topics = {INPUT_TOPIC}, groupId = "arbitrary-group-id")
    public void consumeMessage(String message) {
        // convert string to json with 4 indent
        JsonNode jsonNode = null;
        String value = null;
        try {
            jsonNode = mapper.readTree(message);
            value = jsonNode.toPrettyString();
//            value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("Input message is not valid json: {}\n", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("received message : {} \n", value);
    }

//    @KafkaListener(topics = {"test-topic", "input-topic"}, groupId = "spring-kafka-consumer-group-2")
//    public void consumeMessageWithHeaders(ConsumerRecord<String, String> consumerRecord) {
//        log.info("received message : {} \nwith headers : {} \nand key : {}\n", consumerRecord.value().toUpperCase(), consumerRecord.headers(), consumerRecord.key());
//    }

    @DltHandler
    public void dltHandler(ConsumerRecord<String, String> consumerRecord) {
        log.info("dltHandler : {} \ntopic : {} \nwith headers : {} \nand key : {}\n", consumerRecord.value().toUpperCase(), consumerRecord.topic(), consumerRecord.headers(), consumerRecord.key());
    }

}
