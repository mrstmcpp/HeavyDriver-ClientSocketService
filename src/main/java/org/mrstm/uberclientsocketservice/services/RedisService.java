package org.mrstm.uberclientsocketservice.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    String getDriverBookingMapping(String driverId);
}
