package com.example.springkafka.kafka;

import com.example.springkafka.avro.UserEvent;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String OUTPUT_TOPIC = "demo-topic-1";
    private final Faker faker;
    private int i = 0;


    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.faker = Faker.instance();
    }


//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(initialDelay = 10_000, fixedDelay = 30_000)
    public void sendMessageWithDelay() {
//        String message = "\"" + faker.dune().quote() + "\"";
//        log.info("sending message : {} \n", message);
//        // kafkaTemplate send
//        kafkaTemplate.send(OUTPUT_TOPIC, message);
//
//        // kafkaTemplate send with headers
//        int i = faker.number().randomDigitNotZero();
//        kafkaTemplate.send(OUTPUT_TOPIC, "key-" + i, message);

        UserEvent message = UserEvent.newBuilder().setName(faker.name().name()).setTimestamp(faker.random().nextLong()).build();
        // kafkaTemplate with ProducerRecord
        ProducerRecord<String, Object> record = new ProducerRecord<>(OUTPUT_TOPIC, "record-key-" + i++, message);
        log.info("Producer partition for {}", kafkaTemplate.partitionsFor(OUTPUT_TOPIC));
        kafkaTemplate.send(record);
    }

//    @Async
    public void sendMessage() {
        String message = faker.ancient().god();
        log.info("message : {} \n", message);
        kafkaTemplate.send(OUTPUT_TOPIC, message);
    }

}
