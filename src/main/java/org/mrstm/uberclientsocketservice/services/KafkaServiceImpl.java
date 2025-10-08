package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.kafkaTopics.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishConfirmedBookingEvent(RideResponseByDriver rideResponseByDriver) {
        try{
            kafkaTemplate.send(KafkaTopics.BOOKING_CONFIRMED , rideResponseByDriver);
            System.out.println("published to booking-confirmed.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
