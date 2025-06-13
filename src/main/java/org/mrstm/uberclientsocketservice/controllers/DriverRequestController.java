package org.mrstm.uberclientsocketservice.controllers;

import org.mrstm.uberclientsocketservice.dto.RideRequestDto;
import org.mrstm.uberclientsocketservice.dto.RideResponseDto;
import org.mrstm.uberclientsocketservice.dto.UpdateBookingRequestDto;
import org.mrstm.uberclientsocketservice.dto.UpdateBookingResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/socket")
@CrossOrigin
public class DriverRequestController {
    private final SimpMessagingTemplate template;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;

    public DriverRequestController(SimpMessagingTemplate template, SimpMessagingTemplate simpMessagingTemplate, RestTemplate restTemplate) {
        this.template = template;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        System.out.println("Reqeust for ride is received in socket");
        sendDriverNewRideRequest(rideRequestDto);
        System.out.println("Req completed" + rideRequestDto.getBookingId());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    public void sendDriverNewRideRequest(RideRequestDto  rideRequestDto) {
        //TODO: currently sending to every driver , should go only to nearby drivers;
        template.convertAndSend("/topic/rideRequest", rideRequestDto);
    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId , RideResponseDto rideResponseDto) { //synchronized is added so that it could be accessible to one client at a moment
//        System.out.println(rideResponseDto.getResponse() + " " + userId);
        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Long.parseLong(userId))
                .bookingStatus("SCHEDULED")
                .build();
        System.out.println("rideResponseHandler " + rideResponseDto.getBookingId());
        ResponseEntity<UpdateBookingResponseDto> res = this.restTemplate.postForEntity("http://localhost:3002/api/v1/booking/" + rideResponseDto.bookingId , requestDto , UpdateBookingResponseDto.class);
        System.out.println(res);
    }
}
