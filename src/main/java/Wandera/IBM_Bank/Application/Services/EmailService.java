package Wandera.IBM_Bank.Application.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;



    public void sendWelcomeEmail(String to, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Thymeleaf context
        Context context = new Context();
        context.setVariable("name", name); // user's name
        String html = templateEngine.process("register-email.html", context);

        helper.setTo(to);
        helper.setSubject("Welcome to Our Application!");
        helper.setText(html, true);

        mailSender.send(message);
    }

    public void transferNotification(String receiverEmail, String getBalance, double receiverName, String senderEmail, double amount, String accountNumber) throws MessagingException {


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set FROM and TO
        helper.setFrom(senderEmail);
        helper.setSubject("Transaction");
        helper.setTo(receiverEmail);


        Context context = new Context();
        context.setVariable("receiverName", receiverName); // receiver's name
        context.setVariable("amount", amount);       // transaction amount
        context.setVariable("balance", getBalance);
        String html = templateEngine.process("transfer-email.html", context);

        helper.setText(html, true);

        mailSender.send(message);
    }
    public void receivedMoneyNotification(
            String receiverUserEmail,
            String receiverUserName,
            double amount,
            double balance
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

      //  helper.setFrom(senderEmail);
        helper.setTo(receiverUserEmail);
        helper.setSubject("Money Received");

        // Thymeleaf context
        Context context = new Context();
        context.setVariable("receiverName", receiverUserName);
        context.setVariable("senderName", receiverUserName);
        context.setVariable("amount", amount);
        context.setVariable("balance", balance);

        String htmlContent = templateEngine.process("receiverNotification.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
    public void withdrawNotification(String receiverUser, double amount, double bankBalance, String agentNumber) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Sent FROM
        helper.setTo(receiverUser);
        helper.setSubject("Withdrawal");


        Context context = new Context();
        context.setVariable("amount", amount);       // transaction amount
        context.setVariable("agentNumber", agentNumber);
        context.setVariable("bankBalance", bankBalance);
        String html = templateEngine.process("withdrawal.html", context);

        helper.setText(html, true);

        mailSender.send(message);
    }

    public void depositNotification(String receiverUserEmail, double amount, String agentNumber,double balance) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiverUserEmail);
        helper.setSubject("Money Received");

        // Thymeleaf context
        Context context = new Context();
        context.setVariable("agentNumber", agentNumber);
        context.setVariable("amount", amount);
        context.setVariable("balance", balance);

        String htmlContent = templateEngine.process("receiverDepositNotification.html", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }


}
