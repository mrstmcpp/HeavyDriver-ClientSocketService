package org.mrstm.uberclientsocketservice.services;

import org.mrstm.uberentityservice.dto.booking.NearbyDriverEvent;
import org.springframework.stereotype.Service;

@Service
public interface SocketService {
    void notifyNearbyDrivers(NearbyDriverEvent nearbyDriverEvent);
}
