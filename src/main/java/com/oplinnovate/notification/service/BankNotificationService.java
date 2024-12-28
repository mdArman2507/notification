package com.oplinnovate.notification.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oplinnovate.notification.kafka.KafkaService;
import com.oplinnovate.notification.model.Notification;
import com.oplinnovate.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankNotificationService {

    private final NotificationRepository notificationRepository;
    private final KafkaService kafkaService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public BankNotificationService(NotificationRepository notificationRepository, KafkaService kafkaService, EmailService emailService, NotificationService notificationService) {
        this.notificationRepository = notificationRepository;
        this.kafkaService = kafkaService;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Transactional
    public void notifyUser(Notification notification) {
//        notificationRepository.save(notification);
        kafkaService.sendMessage("bank_notifications", notification.getMessage());

//        if (notification.isUrgent()) {
//            notificationService.sendSms(notification.getRecipient(), notification.getMessage());
//        } else {
//            emailService.sendEmail(notification.getRecipient(), "Bank Notification", notification.getMessage());
//        }
    }


}