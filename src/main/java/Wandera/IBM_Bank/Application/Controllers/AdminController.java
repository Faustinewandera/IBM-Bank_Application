package Wandera.IBM_Bank.Application.Controllers;


import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.AgentAccountRequest;
import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.AgentAccountResponse;
import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.InitialTopUpRequest;
import Wandera.IBM_Bank.Application.Dtos.TransactionDto.TransactionResponse;
import Wandera.IBM_Bank.Application.Dtos.UserDto.UserRequest;
import Wandera.IBM_Bank.Application.Dtos.UserDto.UserResponse;
import Wandera.IBM_Bank.Application.Services.AgentAccountService;
import Wandera.IBM_Bank.Application.Services.TransactionServiceImplimentation;
import Wandera.IBM_Bank.Application.Services.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImplementation userService;
    private final TransactionServiceImplimentation transactionService;
    private final AgentAccountService agentAccountService;

    @PostMapping("/creatUserByAdmin")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllUser")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("getByIdNumber/{id}")
    public UserResponse getUserByIdNumber(@PathVariable String idNumber) {
        return userService.getUserByIdNumber(idNumber);
    }

    @PutMapping("update/{id}")
    public UserResponse updateUserByIdNumber(@PathVariable String idNumber, @RequestBody UserRequest userRequest) {
        return userService.updateUser(idNumber,userRequest);
    }

    @GetMapping("getAllTransaction")
    public List<TransactionResponse> getAllTransactionHistory(@PathVariable String accountNumber) {
        return transactionService.getAllTransactionHistory(accountNumber);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public List<TransactionResponse> getByAccountNumber(@PathVariable String accountNumber) {
        return transactionService.getByAccountNumber(accountNumber);
    }

    @PostMapping("/generateAccount")
    public AgentAccountResponse createAgentAccount(@RequestBody AgentAccountRequest agentAccountRequest, @RequestParam String idNumber) {
        return agentAccountService.createAgentAccount(agentAccountRequest,idNumber);

    }

    @PostMapping("/initialTopUpToAgentAccountByAdmin")
    public ResponseEntity<String> topFloatBalanceByBank(@RequestBody InitialTopUpRequest initialTopUpRequest) {
        return agentAccountService.topFloatBalanceByBank(initialTopUpRequest);
    }

}
