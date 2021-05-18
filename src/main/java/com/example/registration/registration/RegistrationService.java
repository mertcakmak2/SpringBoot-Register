package com.example.registration.registration;

import com.example.registration.user.User;
import com.example.registration.user.UserService;
import com.example.registration.registration.token.ConfirmationToken;
import com.example.registration.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = EmailValidator.validEmail(request.getEmail());
        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }
        String token =  userService.signUpUser(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
       // String link = "http://localhost:5002/api/registration/confirm?token="+token;
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

}
