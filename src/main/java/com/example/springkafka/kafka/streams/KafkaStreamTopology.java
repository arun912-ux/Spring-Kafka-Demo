package com.example.springkafka.kafka.streams;

import com.example.springkafka.avro.UserEvent;
import com.example.springkafka.avro.UserKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class KafkaStreamTopology {

    private final Serde<UserKey> keySerde;
    private final Serde<UserEvent> valueSerde;

    public KafkaStreamTopology(Serde<UserKey> keySerde, Serde<UserEvent> valueSerde) {
        this.keySerde = keySerde;
        this.valueSerde = valueSerde;
    }

    @Bean
    public KStream<UserKey, UserEvent> kStream(StreamsBuilder builder) {

        KStream<UserKey, UserEvent> stream = builder.stream(
                "input-topic",
                Consumed.with(keySerde, valueSerde)
        );

        KStream<UserKey, UserEvent> processed = stream.mapValues((userKey, userEvent) ->
                {
                    log.info("Processing userKey: {} & userEvent: {}", userKey, userEvent);
                    return UserEvent.newBuilder()
                            .setUserId("id").setEventType("Type").setEventTime(1000000L).setMetadata(new HashMap<>()).build();
                }
        );

        processed.to(
                "output-topic",
                Produced.with(keySerde, valueSerde)
        );

        return stream;
    }

}
