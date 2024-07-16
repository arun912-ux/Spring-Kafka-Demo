//package com.example.springkafka.kafka;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.CompletableFuture;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ContextConfiguration(classes = {ProducerService.class})
//@ExtendWith(SpringExtension.class)
//class ProducerServiceDiffblueTest {
//    @MockBean
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private ProducerService producerService;
//
//    /**
//     * Method under test: {@link ProducerService#sendMessage()}
//     */
//    @Test
//    void testSendMessage() {
//        when(kafkaTemplate.send(Mockito.<String>any(), Mockito.<String>any())).thenReturn(new CompletableFuture<>());
//        producerService.sendMessage();
//        verify(kafkaTemplate).send(Mockito.<String>any(), Mockito.<String>any());
//        assertEquals(3, producerService.faker.lorem().words().size());
//    }
//}
