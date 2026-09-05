package com.example.springkafka.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProducerServiceDiffblueTest {

    @Mock
    private KafkaTemplate<Object, Object> kafkaTemplate;
    private ProducerService producerService;

    @BeforeEach
    void setup() {
        producerService = new ProducerService(kafkaTemplate);
    }



    @Test
    void testSendMessage() {
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(new CompletableFuture<>());
        producerService.sendMessage();
        verify(kafkaTemplate).send(anyString(), anyString());
        assertEquals(3, producerService.faker.lorem().words().size());
    }
}
