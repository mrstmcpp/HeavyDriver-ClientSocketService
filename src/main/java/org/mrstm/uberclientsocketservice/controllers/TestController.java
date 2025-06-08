package org.mrstm.uberclientsocketservice.controllers;

import org.mrstm.uberclientsocketservice.dto.ChatRequest;
import org.mrstm.uberclientsocketservice.dto.ChatResponse;
import org.mrstm.uberclientsocketservice.dto.TestRequest;
import org.mrstm.uberclientsocketservice.dto.TestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@Controller
@CrossOrigin
public class TestController {
    private final SimpMessagingTemplate template;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate template, SimpMessagingTemplate simpMessagingTemplate) {
        this.template = template;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message){
        System.out.println("Ping received " + message.getData());
        return TestResponse.builder().data("Received").build();
    }

//    @SendTo("/topic/scheduled")
//    @Scheduled(fixedDelay = 2000)
//    public String scheduledMessage(){
//        template.convertAndSend("/topic/scheduled", TestResponse.builder().data("Received scheduled msg from server").build());
//        return "Scheduled";
//    }

    //public chat
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatResponse chatMessage(ChatRequest chatRequest){
        return ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();
    }

    //prvate chat
    @MessageMapping("/privateChat/{room}/{userId}")
    @SendTo("/topic/privateMessage/{room}/{userId}")
    public void privateChatMessage(@DestinationVariable String room, @DestinationVariable String userId ,  ChatRequest chatRequest){
        ChatResponse res = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();

        simpMessagingTemplate.convertAndSend("/queue/privateMessage/" + room + "/" + userId, res);
    }
}
