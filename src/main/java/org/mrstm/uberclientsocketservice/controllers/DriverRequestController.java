package org.mrstm.uberclientsocketservice.controllers;

import org.mrstm.uberclientsocketservice.Producers.KafkaProducerService;
import org.mrstm.uberclientsocketservice.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
@RequestMapping("/api/socket")
@CrossOrigin
public class DriverRequestController {
    private final SimpMessagingTemplate template;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;

    private final Map<Long , CompletableFuture<Boolean>> bookingFutures = new ConcurrentHashMap<>();


    public DriverRequestController(SimpMessagingTemplate template, SimpMessagingTemplate simpMessagingTemplate, RestTemplate restTemplate, KafkaProducerService kafkaProducerService) {
        this.template = template;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = restTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        System.out.println("Reqeust for ride is received in socket");
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        bookingFutures.put(rideRequestDto.getBookingId() , future);
        sendDriverNewRideRequest(rideRequestDto);

        try{
            Boolean result = future.get(30 , TimeUnit.SECONDS);
            return new ResponseEntity<>(result , HttpStatus.OK);
        }catch (TimeoutException e){
            System.out.println("Timeout waiting for drivers.");
            return new ResponseEntity<>(false , HttpStatus.REQUEST_TIMEOUT);
        }catch (Exception e){
            return new ResponseEntity<>(false , HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            bookingFutures.remove(rideRequestDto.getBookingId());
        }
    }

    public void sendDriverNewRideRequest(RideRequestDto rideRequestDto) {
        //TODO: currently sending to every driver , should go only to nearby drivers;
        System.out.println("Our booking id from sendDriverNewRideRequest : " + rideRequestDto.getBookingId());
        template.convertAndSend("/topic/rideRequest", rideRequestDto);

    }

    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable Long userId , RideResponseDto responseDto) { //synchronized is added so that it could be accessible to one client at a moment
//        System.out.println(rideResponseDto.getResponse() + " " + userId);
        System.out.println("Driver accepted booking: " + responseDto.getBookingId() + " by driverId= " + userId);
        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId((userId))
                .bookingStatus("SCHEDULED")
                .build();
        try {
            ResponseEntity<DriverAcceptedResponseDto> res =
                    restTemplate.postForEntity(
                            "http://localhost:3002/api/v1/booking/" + responseDto.getBookingId(),
                            requestDto,
                            DriverAcceptedResponseDto.class
                    );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Headers: " + e.getResponseHeaders());
            System.err.println("Raw response body: \n" + e.getResponseBodyAsString());
            throw e;
        }

        CompletableFuture<Boolean> future = bookingFutures.get(responseDto.getBookingId());
        if(future != null){
            future.complete(true);
        }

    }


    @GetMapping("")
    public void help(){
        kafkaProducerService.publishMessage("test-topic" , "Hello kafka");
    }
}
