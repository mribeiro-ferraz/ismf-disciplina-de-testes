package com.mackleaps.formium.event.listener;

import com.mackleaps.formium.event.OnSendInviteAuthEvent;
import com.mackleaps.formium.model.auth.User;
import com.mackleaps.formium.model.auth.VerificationToken;
import com.mackleaps.formium.repository.auth.TokenRepository;
import com.mackleaps.formium.service.auth.EmailSenderServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource("classpath:messages.properties")
public class InviteAuthListener implements ApplicationListener<OnSendInviteAuthEvent> {

	@Autowired
	private Environment messageSource;
	
    final Logger logger = LoggerFactory.getLogger(OnSendInviteAuthEvent.class);

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailSenderServiceInterface emailService;

    @Override
    public void onApplicationEvent(OnSendInviteAuthEvent event) {
        this.sendInvite(event);
    }

    private void sendInvite(OnSendInviteAuthEvent event) {

        User user = event.getUser();
        VerificationToken token = new VerificationToken(user, 60 * 24 * 3);

        tokenRepository.saveAndFlush(token);

        String recipientAddress = user.getEmailAddress();
        String subject = "An invite for you from MackLeaps";

        String message = "You're invited to join our application team.\n" +
                "To register your account just click on the link and complete your registration: \n";

        String completeUrl = messageSource.getProperty("config.complete-url");
        String confirmationUrl = completeUrl + "/registration.html?token=" + token;

        message = message + " rn" + confirmationUrl;

        emailService.sendSimpleEmailMessage(recipientAddress, subject, message);
    }
}
