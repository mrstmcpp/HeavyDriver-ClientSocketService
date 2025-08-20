package org.mrstm.uberclientsocketservice.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class NotificationsTestController {
    private final SimpMessagingTemplate template;
    public NotificationsTestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @GetMapping("/notify")
    public void sendTest() {
        template.convertAndSend("/topic/notifications", "fasdfasf");
    }
}
