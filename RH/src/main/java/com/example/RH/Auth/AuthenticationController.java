package com.example.RH.Auth;

import com.example.RH.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ){
        return authenticationService.register(request);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(

            @RequestBody AuthenticationRequest request
    ){
        return authenticationService.authenticate(request);
    }

    @GetMapping("/validate-email")
    public ResponseEntity<Boolean> validateEmail(@RequestParam String email) {
        boolean isTaken = userRepository.findByEmail(email).isPresent();

        return ResponseEntity.ok(isTaken);
    }


}
