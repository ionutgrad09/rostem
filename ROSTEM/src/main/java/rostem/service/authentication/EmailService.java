package rostem.service.authentication;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rostem.model.users.InactiveUser;

@Service
public class EmailService {

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    private MimeMessage message() {
        Properties props = new Properties();

        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        //Establishing a session with required user details
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("rostemplatform@gmail.com", "Parolarostem2019!");
            }
        });

        return new MimeMessage(session);
    }

    void sendRegisterMail(InactiveUser u) throws MessagingException {
        MimeMessage msg = message();
        String to = u.getEmail();
        String url;
        if (System.getenv("IS_PRODUCTION") == null) {
            url = "http://localhost:3000/activate/";
        } else {
            url = "https://rostem.herokuapp.com/activate/";
        }
        InternetAddress[] address = InternetAddress.parse(to, true);
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject("ROSTEM register - noreply");
        msg.setSentDate(new Date());
        msg.setText("Dear " + u.getUsername() + ", thank you for registering to ROSTEM platform.\n" +
                "Activation link: " + url + u.getId() + " \n");
        msg.setHeader("XPriority", "1");
        Transport.send(msg);
        logger.info("Mail has been sent successfully");
    }
}
