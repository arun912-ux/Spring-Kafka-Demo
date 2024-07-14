package com.example.springkafka.kafka;

import net.datafaker.Faker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    Faker faker = Faker.instance();

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void sendMessageWithDelay() {
        String message = faker.dune().quote();
        System.out.println("sending message = " + message + "\n");
        kafkaTemplate.send("test-topic", message);
    }

    public void sendMessage() {
        String message = faker.ancient().god();
        System.out.println("message = " + message);
        kafkaTemplate.send("test-topic", message);
    }

}
