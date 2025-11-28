package org.mrstm.uberclientsocketservice.Consumers;

import org.mrstm.uberclientsocketservice.services.SocketService;
import org.mrstm.uberentityservice.dto.booking.ConfirmedNotificationToPassengerDto;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedBookingNotificationConsumer {
    private final SocketService socketService;


    public ConfirmedBookingNotificationConsumer(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(topics = KafkaTopics.BOOKING_CONFIRMED_NOTIFICATION, groupId = "socket-group")
    public void sendConfirmedNotificationToPassenger(ConfirmedNotificationToPassengerDto confirmedNotificationToPassengerDto){
        System.out.println("Booking confirmed notiifcation received in socket");
        socketService.notifyAboutBookingToPassenger(confirmedNotificationToPassengerDto);
    };
}
