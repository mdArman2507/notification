package com.oplinnovate.notification.controller;

import com.oplinnovate.notification.model.Notification;
import com.oplinnovate.notification.service.BankNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final BankNotificationService bankNotificationService;

    public NotificationController(BankNotificationService bankNotificationService) {
        this.bankNotificationService = bankNotificationService;
    }

    @PostMapping
    public void createNotification(@RequestBody Notification notification) {
        bankNotificationService.notifyUser(notification);
    }

}