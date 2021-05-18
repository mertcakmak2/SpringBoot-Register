package com.example.registration.user;

import com.example.registration.registration.token.ConfirmationToken;
import com.example.registration.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(User user){
        boolean userExist = userRepository.findByEmail(user.getEmail()).isPresent();
        if(userExist){
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15L),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
