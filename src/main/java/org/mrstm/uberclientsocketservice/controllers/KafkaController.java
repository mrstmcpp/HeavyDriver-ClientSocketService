package org.mrstm.uberclientsocketservice.controllers;


import org.mrstm.uberclientsocketservice.Consumers.KafkaConsumerService;
import org.mrstm.uberclientsocketservice.Producers.KafkaProducerService;
import org.mrstm.uberclientsocketservice.dto.RideRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    public KafkaController(KafkaProducerService kafkaProducerService , KafkaConsumerService kafkaConsumerService){
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("")
    public ResponseEntity<String> publishMessage(@RequestParam("message") String message){
        kafkaProducerService.publishMessage("test-topic" , message);
        return ResponseEntity.ok("Message published to test topic.");
    }

//    @PostMapping("/update-driver-location")
//    public ResponseEntity<String> updateDriverLocation(String message){
//        kafkaProducerService.publishMessage("driver-location" , message);
//        return ResponseEntity.ok("Updated");
//    }
}
