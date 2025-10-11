package org.mrstm.uberclientsocketservice.Consumers;

import org.mrstm.uberclientsocketservice.services.SocketService;
import org.mrstm.uberentityservice.dto.location.DriverLocation;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DriverLocationConsumer {
    private final SocketService socketService;

    public DriverLocationConsumer(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(topics = KafkaTopics.DRIVER_LOCATION , groupId = "socket-group")
    public void consumerDriverLocation(DriverLocation location){ //this will send real-time location to passenger using socket
        System.out.println("Received at socket side: " + location.getDriverId() + " " + location.getLatitude() + " " + location.getLongitude());
        socketService.broadcastOngoingRideLocation();
    }
}
