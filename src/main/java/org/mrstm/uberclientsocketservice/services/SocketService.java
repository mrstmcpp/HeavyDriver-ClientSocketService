package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.ConfirmedNotificationToPassengerDto;
import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.dto.booking.UpdateBookingResponseDto;
import org.mrstm.uberentityservice.dto.location.DriverLocation;
import org.springframework.stereotype.Service;

@Service
public interface SocketService {
    void notifyNearbyDrivers(NearbyDriverEvent nearbyDriverEvent);
    void notifyAboutBookingToPassenger(ConfirmedNotificationToPassengerDto confirmedNotificationToPassengerDto);
    void broadcastOngoingRideLocation(DriverLocation location);
    void notifyBookingUpdatesToPassenger(UpdateBookingResponseDto updateBookingResponseDto);
}
