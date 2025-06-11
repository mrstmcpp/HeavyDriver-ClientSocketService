package org.mrstm.uberclientsocketservice.controllers;

import org.mrstm.uberclientsocketservice.dto.RideRequestDto;
import org.mrstm.uberclientsocketservice.dto.RideResponseDto;
import org.mrstm.uberclientsocketservice.dto.TestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/socket")
@CrossOrigin
public class DriverRequestController {
    private final SimpMessagingTemplate template;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public DriverRequestController(SimpMessagingTemplate template, SimpMessagingTemplate simpMessagingTemplate) {
        this.template = template;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        sendDriverNewRideRequest(rideRequestDto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    public String sendDriverNewRideRequest(RideRequestDto  rideRequestDto) {
        //TODO: currently sending to every driver , should go only to nearby drivers;
        template.convertAndSend("/topic/rideRequest", rideRequestDto);
        return "Scheduled";
    }

    @MessageMapping("/rideResponse")
    public void rideResponseHandler(RideResponseDto rideResponseDto) {
        System.out.println(rideResponseDto.getResponse());

    }
}
