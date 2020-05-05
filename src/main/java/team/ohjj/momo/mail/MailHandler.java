package team.ohjj.momo.mail;

import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class MailHandler {
    private JavaMailSender sender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.sender = javaMailSender;
        message = javaMailSender.createMimeMessage();
        messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 두번째 파라미터는 다수의 사람에게 보낼 수 있는 설정
    }

    // 메일 송신자
    public void setFrom(String email, String name) throws UnsupportedEncodingException, MessagingException {
        messageHelper.setFrom(email, name);
    }

    // 메일 수신자
    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    // 메일 제목
    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    // 메일의 내용, 두번째 파라미터는 html 적용 여부
    public void setText(String text) throws MessagingException {
        messageHelper.setText(text, true);
    }

    // 간단한 첨부파일 전송
    public void addInline(String contentId, Resource resource) throws MessagingException {
        messageHelper.addInline(contentId, resource);
    }

    public void addInline(String contentId, File file) throws MessagingException {
        messageHelper.addInline(contentId, file);
    }

    public void addInline(String contentId, DataSource dataSource) throws MessagingException {
        messageHelper.addInline(contentId, dataSource);
    }

    // 메일 전송
    public void send() {
        try {
            sender.send(message);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
