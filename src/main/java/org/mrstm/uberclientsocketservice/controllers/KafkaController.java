package org.mrstm.uberclientsocketservice.controllers;


import org.mrstm.uberclientsocketservice.Consumers.NearbyDriverConsumer;
import org.mrstm.uberclientsocketservice.Producers.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    private final KafkaProducerService kafkaProducerService;
    private final NearbyDriverConsumer kafkaConsumerService;

    public KafkaController(KafkaProducerService kafkaProducerService , NearbyDriverConsumer kafkaConsumerService){
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/update-driver-location")
    public ResponseEntity<String> updateDriverLocation(String message){
        kafkaProducerService.publishMessage("driver-location" , message);
        return ResponseEntity.ok("Updated");
    }
}
