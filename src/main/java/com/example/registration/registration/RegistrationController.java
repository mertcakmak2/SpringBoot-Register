package com.example.registration.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(value = "")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(value = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
