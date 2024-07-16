package com.example.springkafka.kafka.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Slf4j
public class StreamProcessor {

    private final ObjectMapper mapper;

    public StreamProcessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean("streamProcess")
    public Function<String, String> streamProcess() {
        log.info("************************* inside streamProcess method.***********************************");
        return (input) -> {
            try {
                JsonNode jsonNode = mapper.readTree(input);
                input = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            } catch (JsonProcessingException e) {
                log.error("Input message is not valid json: {}. \nYet processing as String ", input);
            }
            input = input.toUpperCase();
            log.info("streamProcess : {}", input);
            return input;
        };
    }







//    @Bean("stringConsumer")
//    public Consumer<String> stringConsumer() {
//        log.info("************************* inside stringConsumer method.***********************************");
//        return (input) -> {
//            input = input.toUpperCase();
//            log.info("stringConsumer : {}", input);
//        };
//    }
//
//
//    @Bean("stringSupplier")
//    public Supplier<String> stringSupplier() {
//        log.info("************************* inside stringSupplier method.***********************************");
//        String hello = "Hello from Supplier";
//        AtomicInteger i = new AtomicInteger();
//        return () -> {
//            log.info("stringSupplier : {}-{}", hello, i.getAndIncrement());
//            return hello+"-"+i.get();
//        };
//    }


}
