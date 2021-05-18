package com.example.registration.registration.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token){
        Optional<ConfirmationToken> existToken = confirmationTokenRepository.findByToken(token);
        return existToken;
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
