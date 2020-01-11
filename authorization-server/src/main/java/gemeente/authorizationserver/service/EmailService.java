package gemeente.authorizationserver.service;

import gemeente.authorization.api.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${content.email.from}")
    private String from;

    @Value("${content.email.from-display}")
    private String fromDisplay;

    @Value("${content.email.resetpassword.subject:Reset your password}")
    private String subject;

    public void sendPasswordReset(final Account account, final ResetToken resetToken) {
        final Context ctx = new Context();
        ctx.setVariable("account", account);
        ctx.setVariable("resetUrl", resetToken.generateUrl());
        ctx.setVariable("replyTo", from);
        sendMessage(account.getEmailAddress(), subject, "recover-password-email-link", ctx);
    }

    void sendMessage(
            String to, String subject, String template, final Context context) {

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        try {
            final MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setSubject(subject);
            message.setFrom(from, fromDisplay);
            message.setTo(to);

            // Create the HTML body using Thymeleaf
            final String htmlContent = this.templateEngine.process(template, context);
            message.setText(htmlContent, true); // true = isHtml

            // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
            // final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
            // message.addInline(imageResourceName, imageSource, imageContentType);

            // Send mail
            this.emailSender.send(mimeMessage);
        }
        catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
