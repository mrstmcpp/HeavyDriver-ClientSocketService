package org.mrstm.uberclientsocketservice.Consumers;

import org.mrstm.uberclientsocketservice.services.SocketService;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.dto.booking.UpdateBookingResponseDto;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingUpdatesConsumer {
    private final SocketService socketService;

    public BookingUpdatesConsumer(SocketService socketService) {
        this.socketService = socketService;
    }

    @KafkaListener(topics = KafkaTopics.BOOKING_UPDATES, groupId = "socket-group")
    public void sendBookingUpdateToPassenger(UpdateBookingResponseDto updateBookingResponseDto){
        System.out.println("Send booking updates : " + updateBookingResponseDto.getBookingId() + " " + updateBookingResponseDto.getBookingStatus());
        socketService.notifyBookingUpdatesToPassenger(updateBookingResponseDto);
    };
}
