package com.example.auth.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Function;

@RequiredArgsConstructor
@Log4j2
public abstract class AbstractKafkaSender<E, A extends SpecificRecord> {
    protected final KafkaTemplate<String, A> kafkaTemplate;
    protected final Function<E, A> mapper;
    protected final String mainTopic;
    protected final String dlqTopic;

    protected void sendEvent(String key, E event) {
        try {
            A avroMessage = mapper.apply(event);
            kafkaTemplate.send(mainTopic, key, avroMessage)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Send failed to {}, redirecting to DLQ", mainTopic, ex);
                            kafkaTemplate.send(dlqTopic, key, avroMessage)
                                    .whenComplete((dlqResult, dlqEx) -> {
                                        if (dlqEx != null) {
                                            log.error("DLQ send also failed for {}", key, dlqEx);
                                        }
                                    });
                        } else {
                            log.info("Sent to {} [{}], partition: {}, offset: {}",
                                    mainTopic, key,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Mapping failed for {}", key, e);
        }
    }
}