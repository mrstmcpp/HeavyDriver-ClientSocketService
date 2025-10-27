package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.ConfirmedNotificationToPassengerDto;
import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.mrstm.uberentityservice.dto.booking.RideResponseByDriver;
import org.mrstm.uberentityservice.dto.booking.UpdateBookingResponseDto;
import org.mrstm.uberentityservice.dto.location.DriverLocation;
import org.mrstm.uberentityservice.dto.notification.RideRequestNotification;
import org.mrstm.uberentityservice.dto.notification.RideUpdateNotification;
import org.mrstm.uberentityservice.models.BookingStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocketServiceImpl implements SocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisService redisService;


    public SocketServiceImpl(SimpMessagingTemplate messagingTemplate, RedisService redisService) {
        this.messagingTemplate = messagingTemplate;
        this.redisService = redisService;
    }

    @Override
    public void notifyNearbyDrivers(NearbyDriverEvent nearbyDriverEvent) { //sedning notification to nearby drivers
        List<DriverLocation> driverLocations = nearbyDriverEvent.getDriverLocationList();
        String bookingId = nearbyDriverEvent.getBookingId();

        for (DriverLocation driverLocation : driverLocations) {
            String driverId = driverLocation.getDriverId();
            RideRequestNotification notification = RideRequestNotification.builder()
                    .type("RIDE_REQUEST")
                    .bookingId(nearbyDriverEvent.getBookingId())
                    .passengerId(nearbyDriverEvent.getPassengerId())
                    .pickupLocation(nearbyDriverEvent.getPickupLocation())
                    .dropLocation(nearbyDriverEvent.getDropLocation())
                    .build();
//            System.out.println(nearbyDriverEvent.getDropLocation().getAddress());
            messagingTemplate.convertAndSend("/topic/driver/" + driverId, notification);
            System.out.println("Notified driver " + driverId + " for booking " + bookingId);
        }
    }

    @Override
    public void notifyAboutBookingToPassenger(ConfirmedNotificationToPassengerDto confirmedNotificationToPassengerDto) { //to inform passenger
        try {
//            RideUpdateNotification notification = RideUpdateNotification.builder()
//                    .bookingId(confirmedNotificationToPassengerDto.getBookingId())
//                    .bookingStatus(BookingStatus.SCHEDULED)
//                    .driverId(confirmedNotificationToPassengerDto.getDriverId())
//                    .build();
            System.out.println("sending notification to passsenger " + confirmedNotificationToPassengerDto.getPassengerId() + " = " + confirmedNotificationToPassengerDto.getBookingId());
            messagingTemplate.convertAndSend("/topic/user/" + confirmedNotificationToPassengerDto.getPassengerId(), confirmedNotificationToPassengerDto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void broadcastOngoingRideLocation(DriverLocation location) {
        try {
            String bookingId = redisService.getDriverBookingMapping(location.getDriverId());
            //just to test
//            messagingTemplate.convertAndSend(
//                    "/topic/driver/3/driver-location",
//                    location
//            );

            if (bookingId == null) {
                System.out.println("No booking found for driver: " + location.getDriverId());
                return;
            }
            messagingTemplate.convertAndSend(
                    "/topic/booking/" + bookingId + "/driver-location",
                    location
            );

            System.out.println("Sent location update for booking " + bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to broadcast driver location", e);
        }
    }

    @Override
    public void notifyBookingUpdatesToPassenger(UpdateBookingResponseDto updateBookingResponseDto) {
        try{
            messagingTemplate.convertAndSend(
                    "/topic/booking/" + updateBookingResponseDto.getBookingId() + "/updates",
                    updateBookingResponseDto
            );
        } catch (Exception e) {
            throw new RuntimeException("Not able to send booking update to passenger. " + e.getMessage());
        }
    }
}
