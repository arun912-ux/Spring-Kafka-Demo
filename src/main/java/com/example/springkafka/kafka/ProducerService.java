package com.example.springkafka.kafka;

import com.example.springkafka.avro.UserEvent;
import com.example.springkafka.avro.UserKey;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Service
public class ProducerService {

    private final KafkaTemplate kafkaTemplate;
    private static final String OUTPUT_TOPIC = "demo-topic-1";
    public final Faker faker;
    private long i = 0;
    @Value("${kafka.producer.enabled:true}")
    private boolean producerEnabled;


    public ProducerService(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.faker = new Faker(Locale.ENGLISH);
    }


//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(initialDelay = 10_000, fixedDelay = 30_000 * 2)
    public void loop() {
        while (i++ < 1_0L && producerEnabled) {
            sendMessageWithDelay();
        }
        i = 0;
    }

    public void sendMessageWithDelay() {
        UserEvent message = UserEvent.newBuilder()
                .setUserId(faker.idNumber().singaporeanUin())
                .setEventType(faker.esports().event())
                .setEventTime(faker.random().nextLong(1_000_000L))
                .setMetadata(new HashMap<>())
                .build();
        UserKey key = UserKey.newBuilder()
                .setUserId(faker.idNumber().peselNumber())
                .setRegion(faker.nigeria().places())
                .build();

        // kafkaTemplate with ProducerRecord
        ProducerRecord<UserKey, UserEvent> record = new ProducerRecord<>(OUTPUT_TOPIC, key, message);
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
