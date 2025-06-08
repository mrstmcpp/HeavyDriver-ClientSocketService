package org.mrstm.uberclientsocketservice.controllers;

import org.mrstm.uberclientsocketservice.dto.TestRequest;
import org.mrstm.uberclientsocketservice.dto.TestResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class TestController {

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message){
        System.out.println("Ping received" + message.getData());
        return TestResponse.builder().data("Received").build();
    }
}
