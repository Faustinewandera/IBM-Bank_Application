package Wandera.IBM_Bank.Application.Controllers;

import Wandera.IBM_Bank.Application.Dtos.UserDto.UserRequest;
import Wandera.IBM_Bank.Application.Dtos.UserDto.UserResponse;
import Wandera.IBM_Bank.Application.Services.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImplementation userService;
   // private final AccountServicesImplementation accountServices;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
        //TODO email validation
    }

    @PutMapping("update/{id}")
    public UserResponse updateUserByIdNumber(@PathVariable String idNumber, @RequestBody UserRequest userRequest) {
        return userService.updateUser(idNumber,userRequest);
    }

}
