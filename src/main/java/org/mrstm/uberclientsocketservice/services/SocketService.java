package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.dto.notification.RideUpdateNotification;
import org.springframework.stereotype.Service;

@Service
public interface SocketService {
    void notifyNearbyDrivers(NearbyDriverEvent nearbyDriverEvent);
    void notifyConfirmedBookingToPassenger(RideResponseByDriver rideResponseByDriver);
}
