package Wandera.IBM_Bank.Application.Controllers;

import Wandera.IBM_Bank.Application.Dtos.AuthDto.RegistrationRequest;
import Wandera.IBM_Bank.Application.Dtos.AuthDto.RegistrationResponse;
import Wandera.IBM_Bank.Application.Jwt.JwtService;
import Wandera.IBM_Bank.Application.Security.AppUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailService userDetailService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegistrationRequest registrationRequest) {
        try{
            authenticate(registrationRequest.getEmail(),registrationRequest.getPassword());
            final UserDetails userDetails=userDetailService.loadUserByUsername(registrationRequest.getEmail());
            String jwtToken=jwtService.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new RegistrationResponse(registrationRequest.getEmail(),jwtToken));


        } catch (BadCredentialsException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Incorrect email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( response);

        }
        catch (DisabledException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( response);

        }
        catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( response);

        }
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
