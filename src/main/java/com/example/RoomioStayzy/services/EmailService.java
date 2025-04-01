package com.example.RoomioStayzy.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String ownerName, String username, String comment, String time, String link) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "    font-family: Arial, sans-serif;" +
                "    line-height: 1.6;" +
                "    background-color: #f9f9f9;" +
                "    margin: 0; padding: 0;" +
                "}" +
                ".container {" +
                "    max-width: 600px;" +
                "    margin: 20px auto;" +
                "    background: #ffffff;" +
                "    border-radius: 8px;" +
                "    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                "    padding: 20px;" +
                "}" +
                "h2 {" +
                "    color: #333333;" +
                "    font-size: 20px;" +
                "    text-align: center;" +
                "}" +
                "p {" +
                "    color: #555555;" +
                "    font-size: 16px;" +
                "    margin: 10px 0;" +
                "}" +
                "ul {" +
                "    padding-left: 20px;" +
                "}" +
                "li {" +
                "    margin: 5px 0;" +
                "}" +
                "a {" +
                "    display: inline-block;" +
                "    margin-top: 15px;" +
                "    padding: 10px 20px;" +
                "    background-color: #007BFF;" +
                "    color: #ffffff;" +
                "    text-decoration: none;" +
                "    border-radius: 4px;" +
                "}" +
                "a:hover {" +
                "    background-color: #0056b3;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2>Phản hồi mới về căn hộ của bạn</h2>" +
                "<p>Chào " + ownerName + ",</p>" +
                "<p>Có người đã gửi phản hồi về căn hộ của bạn với nội dung như sau:</p>" +
                "<ul>" +
                "<li><b>Tên:</b> " + username + "</li>" +
                "<li><b>Nội dung:</b> " + comment + "</li>" +
                "<li><b>Thời gian:</b> " + time + "</li>" +
                "</ul>" +
                "<p><a href='" + link + "'>Click vào đây để xem chi tiết</a></p>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("vhquynh0802@gmail.com");
        mailSender.send(mimeMessage);
    }
}
