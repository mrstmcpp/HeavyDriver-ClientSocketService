package org.mrstm.uberclientsocketservice.Producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;

    }

    public void publishMessage(String topic , String message){
        kafkaTemplate.send(topic , message);
    }

}
