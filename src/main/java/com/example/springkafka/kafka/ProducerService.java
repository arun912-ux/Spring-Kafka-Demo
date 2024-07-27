package com.example.springkafka.kafka;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String OUTPUT_TOPIC = "input-topic";
    Faker faker = Faker.instance();


    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(initialDelay = 3_000, fixedDelay = 30_000)
    public void sendMessageWithDelay() {
        String message = "\"" + faker.dune().quote() + "\"";
        log.info("sending message : {} \n", message);
        // kafkaTemplate send
        kafkaTemplate.send(OUTPUT_TOPIC, message);

        // kafkaTemplate send with headers
        int i = faker.number().randomDigitNotZero();
        kafkaTemplate.send(OUTPUT_TOPIC, "key-" + i, message);

        // kafkaTemplate with ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<>(OUTPUT_TOPIC, "record-key-" + i, message);
        kafkaTemplate.send(record);
    }

    @Async
    public void sendMessage() {
        String message = faker.ancient().god();
        log.info("message : {} \n", message);
        kafkaTemplate.send(OUTPUT_TOPIC, message);
    }

}
