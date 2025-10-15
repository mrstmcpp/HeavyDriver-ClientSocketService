package org.mrstm.uberclientsocketservice.Consumers;

import org.mrstm.uberclientsocketservice.services.SocketService;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedBookingConsumer {

    private final SocketService socketService;

    public ConfirmedBookingConsumer(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(topics = KafkaTopics.BOOKING_CONFIRMED, groupId = "socket-group")
    public void sendConfirmedNotificationToPassenger(RideResponseByDriver rideResponseByDriver){
        System.out.println("ready to send notification received in socket");
        socketService.notifyConfirmedBookingToPassenger(rideResponseByDriver);
    };
}
