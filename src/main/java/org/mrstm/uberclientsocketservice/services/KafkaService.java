package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.springframework.stereotype.Service;

@Service
public interface KafkaService {
    void publishConfirmedBookingEvent(RideResponseByDriver rideResponseByDriver);
}
