package org.mrstm.uberclientsocketservice.Consumers;

import org.mrstm.uberclientsocketservice.services.SocketService;
import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NearbyDriverConsumer {
    private final SocketService socketService;

    public NearbyDriverConsumer(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(topics = KafkaTopics.NEARBY_DRIVERS , groupId = "${spring.kafka.consumer.group-id}")
    public void consumeNearbyDrivers(NearbyDriverEvent payload){
        //got list not forward it to socket service to send notifications;
        socketService.notifyNearbyDrivers(payload);
    }
}
