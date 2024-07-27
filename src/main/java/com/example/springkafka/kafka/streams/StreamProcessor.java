//package com.example.springkafka.kafka.streams;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.retry.support.RetryTemplate;
//import org.springframework.retry.support.RetryTemplateBuilder;
//import org.springframework.stereotype.Service;
//
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//@Service
//@Slf4j
//public class StreamProcessor {
//
//    private final ObjectMapper mapper;
//
//    public StreamProcessor(ObjectMapper mapper) {
//        this.mapper = mapper;
//    }
//
//
//
//    @Bean("streamProcess")
//    public Function<String, String> streamProcess() {
//        log.info("************************* inside streamProcess method.***********************************");
//        return (input) -> {
//            try {
//                JsonNode jsonNode = mapper.readTree(input);
//                input = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
//            } catch (JsonProcessingException e) {
//                log.error("Input message is not valid json: {}. \nYet processing as String ", input);
//                throw new RuntimeException(e);
//            }
//            input = input.toUpperCase();
//            log.info("streamProcess : {}", input);
//            return input;
//        };
//    }
//
//
//    @Bean("errorHandler")
//    public Function<String, String> errorHandler() {
//        log.info("************************* inside errorHandler method.***********************************");
//        return (error) -> {
//            log.info("errorHandler : {}", error);
////            try {
////                log.info("errorHandler : {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(error)));
////            } catch (JsonProcessingException e) {
////                throw new RuntimeException(e);
////            }
//            return error;
//        };
//    }
//
//
////    @Bean
////    public DlqDestinationResolver dlqDestinationResolver() {
////        return (rec, ex) -> {
////            if (ex.getMessage().contains("com.fasterxml.jackson.core.io.JsonEOFException")) {
////                return "streams-topic-error";
////            }
////            else {
////                return "topic2-dlq";
////            }
////        };
////    }
//
//
//    @Bean("retryTemplate")
//    @StreamRetryTemplate
//    public RetryTemplate retryTemplate() {
//        return new RetryTemplateBuilder()
//                .maxAttempts(3)
//                .exponentialBackoff(2000, 2, 30_000)
//                .build();
//    }
//
//
////    @Bean("stringConsumer")
////    public Consumer<String> stringConsumer() {
////        log.info("************************* inside stringConsumer method.***********************************");
////        return (input) -> {
////            input = input.toUpperCase();
////            log.info("stringConsumer : {}", input);
////        };
////    }
////
////
////    @Bean("stringSupplier")
////    public Supplier<String> stringSupplier() {
////        log.info("************************* inside stringSupplier method.***********************************");
////        String hello = "Hello from Supplier";
////        AtomicInteger i = new AtomicInteger();
////        return () -> {
////            log.info("stringSupplier : {}-{}", hello, i.getAndIncrement());
////            return hello+"-"+i.get();
////        };
////    }
//
//
//}
