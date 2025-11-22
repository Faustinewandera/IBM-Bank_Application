package Wandera.IBM_Bank.Application.Controllers;

import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.DepositRequest;
import Wandera.IBM_Bank.Application.Services.AccountServicesImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
public class AgentController {

    private final AccountServicesImplementation accountServicesImplementation;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest, String agentNumber) {
        return accountServicesImplementation.deposit(depositRequest,agentNumber);

    }

}
