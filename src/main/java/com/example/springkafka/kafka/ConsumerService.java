package com.example.springkafka.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {


    private static final String INPUT_TOPIC = "demo-topic-1";
    private final ObjectMapper mapper = new ObjectMapper();
    @Value("${kafka.producer.enabled:true}")
    private boolean producerEnabled;

    public ConsumerService() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    //    @RetryableTopic(
//            attempts = "2",
//            backoff = @Backoff(
//                    delay = 10_000,
//                    multiplier = 2
//            ),
//            dltTopicSuffix = "-dlt",
//            dltStrategy = DltStrategy.FAIL_ON_ERROR
//    )
//    @KafkaListener(topics = {"test-topic", INPUT_TOPIC}, groupId = "spring-kafka-consumer-group-id")
    @KafkaListener(topics = {INPUT_TOPIC}, groupId = "arbitrary-group-id")
    public void consumeMessageWithHeaders(ConsumerRecord<Object, Object> consumerRecord) {
        if (!producerEnabled) {
            log.info("received message : {} \nwith headers : {} \nand key : {} - hashcode : {}\npartition : {}\n",
                    consumerRecord.value().toString().toUpperCase(), consumerRecord.headers(), consumerRecord.key(),
                    Utils.toPositive(Utils.murmur2(consumerRecord.key().toString().getBytes())), consumerRecord.partition());
        }
    }

}
