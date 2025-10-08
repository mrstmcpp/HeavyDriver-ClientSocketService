package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.dto.location.DriverLocation;
import org.mrstm.uberentityservice.dto.notification.RideRequestNotification;
import org.mrstm.uberentityservice.dto.notification.RideUpdateNotification;
import org.mrstm.uberentityservice.models.BookingStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketServiceImpl implements SocketService{
    private final SimpMessagingTemplate messagingTemplate;

    public SocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyNearbyDrivers(NearbyDriverEvent nearbyDriverEvent) {
        List<DriverLocation> driverLocations = nearbyDriverEvent.getDriverLocationList();
        Long bookingId = Long.parseLong(nearbyDriverEvent.getBookingId());

        for(DriverLocation driverLocation : driverLocations){
            Long driverId = Long.parseLong(driverLocation.getDriverId());
            RideRequestNotification notification = RideRequestNotification.builder()
                    .bookingId(nearbyDriverEvent.getBookingId())
                    .passengerId(nearbyDriverEvent.getPassengerId())
                    .pickupLocation(nearbyDriverEvent.getPickupLocation())
                    .dropLocation(nearbyDriverEvent.getDropLocation())
                    .build();

            messagingTemplate.convertAndSend("/topic/driver/" + driverId , notification);
            System.out.println("Notified driver " + driverId + " for booking " + bookingId);
        }
    }

    @Override
    public void notifyConfirmedBookingToPassenger(RideResponseByDriver rideResponseByDriver) {
        try{
            RideUpdateNotification notification = RideUpdateNotification.builder()
                    .bookingId(rideResponseByDriver.getBookingId())
                    .bookingStatus(BookingStatus.SCHEDULED)
                    .driverId(rideResponseByDriver.getDriverId())
                    .build();
            System.out.println("sending notification to passsenger " + rideResponseByDriver.getPassengerId() + " = " + rideResponseByDriver.getBookingId());
            messagingTemplate.convertAndSend("/topic/user/" + rideResponseByDriver.getPassengerId(), notification);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
