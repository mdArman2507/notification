package com.oplinnovate.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oplinnovate.notification.model.Notification;
import com.oplinnovate.notification.model.NotificationType;
import com.oplinnovate.notification.repository.NotificationRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {



    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    public NotificationService(EmailService emailService, NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
    }

    public void sendSms(String phoneNumber, String message) {
        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(fromPhoneNumber),
                message
        ).create();
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }

    public void sendPushNotification(String deviceToken, String message) {
        // Implement push notification logic
        System.out.println("Sending Push Notification to " + deviceToken + ": " + message);
    }

    @KafkaListener(topics = "bank_notifications", groupId = "group_id")
    public void listen(String message) {
        // Parse the incoming message
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Notification notification = objectMapper.readValue(message, Notification.class);

            // Save notification to the database
            notificationRepository.save(notification);

            // Determine the type of notification and send it
            if (notification.isUrgent()) {
                // Send SMS or Push Notification for urgent notifications
                if (notification.getType().equals(NotificationType.SMS.name())) {
                    sendSms(notification.getRecipient(), notification.getMessage());
                } else if (notification.getType().equals(NotificationType.PUSH.name())) {
                    sendPushNotification(notification.getRecipient(), notification.getMessage());
                }
            } else {
                // Send Email for non-urgent notifications
                if (notification.getType().equals(NotificationType.EMAIL.name())) {
                    emailService.sendEmail(notification.getRecipient(), "Bank Notification", notification.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}