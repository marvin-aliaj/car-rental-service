package com.spring.jdbc.car.rental.controller;


import com.spring.jdbc.car.rental.exception.ForbiddenException;
import com.spring.jdbc.car.rental.service.AuthenticationService;
import com.twilio.twiml.voice.Prompt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authService = authenticationService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> authenticate(@RequestBody Map<String, String> user
            , @RequestHeader(value = "x-api-key") String apiKey
    ) {
        try {
            if (!authService.hasAccess(apiKey)) {
                throw new ForbiddenException("Action not allowed");
            }
            String response = authService.validateUser(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

