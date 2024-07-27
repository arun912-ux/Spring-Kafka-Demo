//package com.example.springkafka.kafka;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.RetryableTopic;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class ConsumerService {
//
//
//    private final ObjectMapper mapper;
//    private static final String INPUT_TOPIC = "input-topic";
//
//    @RetryableTopic(attempts = "5")
//    @KafkaListener(topics = {INPUT_TOPIC}, groupId = "arbitrary-group-id")
//    public void consumeMessage(String message) throws JsonProcessingException {
//        // convert string to json with 4 indent
//        JsonNode jsonNode = mapper.readTree(message);
//        String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
//        log.info("received message : {} \n", value);
//    }
//
//    @KafkaListener(topics = {"test-topic"}, groupId = "spring-kafka-consumer-group-22")
//    public void consumeMessageWithHeaders(ConsumerRecord<String, String> consumerRecord) {
//        log.info("received message : {} with headers : {}\n", consumerRecord.value().toUpperCase(), consumerRecord.headers());
//    }
//
//}
