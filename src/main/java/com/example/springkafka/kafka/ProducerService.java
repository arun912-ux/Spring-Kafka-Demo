package com.example.springkafka.kafka;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String OUTPUT_TOPIC = "test-topic";
    Faker faker = Faker.instance();


    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(initialDelay = 3000, fixedDelay = 30_000)
    public void sendMessageWithDelay() {
        String message = faker.dune().quote();
        log.info("sending message : {} \n", message);
        kafkaTemplate.send(OUTPUT_TOPIC, message);
    }

    public void sendMessage() {
        String message = faker.ancient().god();
        log.info("message : {} \n", message);
        kafkaTemplate.send(OUTPUT_TOPIC, message);
    }

}
