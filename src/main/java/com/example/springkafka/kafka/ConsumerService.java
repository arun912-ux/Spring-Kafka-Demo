package com.example.springkafka.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ConsumerService {


    private static final String INPUT_TOPIC = "demo-topic-1";
    private final ObjectMapper mapper = new ObjectMapper();

    public ConsumerService() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(
                    delay = 10_000,
                    multiplier = 2
            ),
            dltTopicSuffix = "--dlt"
    )
    @KafkaListener(topics = {INPUT_TOPIC}, groupId = "arbitrary-group-id")
//    public void consumeMessage(String message) {
//        // convert string to json with 4 indent
//        JsonNode jsonNode = null;
//        String value = null;
//        try {
//            jsonNode = mapper.readTree(message);
//            value = jsonNode.toPrettyString();
////            value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
//        } catch (JsonProcessingException e) {
//            log.error("Input message is not valid json: {}\n", e.getMessage());
//            throw new RuntimeException(e);
//        }
//        log.info("received message : {} \n", value);
//    }

//    @KafkaListener(topics = {"test-topic", INPUT_TOPIC}, groupId = "spring-kafka-consumer-group-id")
    public void consumeMessageWithHeaders(ConsumerRecord<Object, Object> consumerRecord) throws IOException {
        log.info("received message : {} \nwith headers : {} \nand key : {} - hashcode : {}\npartition : {}\n", consumerRecord.value().toString().toUpperCase(), consumerRecord.headers(), consumerRecord.key(),
                    Utils.toPositive(Utils.murmur2(consumerRecord.key().toString().getBytes())), consumerRecord.partition());
    }

}
