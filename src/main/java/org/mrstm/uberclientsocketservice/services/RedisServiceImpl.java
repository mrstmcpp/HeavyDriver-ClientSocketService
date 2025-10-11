package org.mrstm.uberclientsocketservice.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService{
    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getDriverBookingMapping(String driverId) {
        try {
            return redisTemplate.opsForValue().get("driver_booking:" + driverId);
        } catch (Exception e) {
            System.err.println("error while retrieving driver-booking from cache: " + e.getMessage());
            return null;
        }
    }
}
