package org.mrstm.uberclientsocketservice.Consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "test-topic" , groupId = "group-1")
    public void listen(String message){
        System.out.println("Msg received " + message);
    }

    @KafkaListener(topics = "booking" , groupId = "group-1")
    public void listenBookings(String message){
        System.out.println("message " + message);
    }

    @KafkaListener(topics = "driver-location" , groupId = "group-1")
    public void listenDriverLocation(String message){
        System.out.println("message " + message);
    }
}
