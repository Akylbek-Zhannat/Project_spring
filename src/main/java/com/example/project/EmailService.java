package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(String to, String bookTitle, String reserveDateTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Ticket Confirmation");
        message.setText("Вы успешно купили билет на фильм \"" + bookTitle +
                "\" Время " + reserveDateTime +
                "\n\nЖдём вас!");
        mailSender.send(message);
        System.out.println("Booking confirmation email sent to " + to);
    }
}
