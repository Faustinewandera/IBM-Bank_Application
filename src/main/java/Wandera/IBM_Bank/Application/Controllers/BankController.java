package Wandera.IBM_Bank.Application.Controllers;

import Wandera.IBM_Bank.Application.Dtos.BankDto.TransferDto.SendMoneyRequest;
import Wandera.IBM_Bank.Application.Dtos.UserDto.WithdrawRequest;
import Wandera.IBM_Bank.Application.Services.AccountServicesImplementation;
import Wandera.IBM_Bank.Application.Services.TransactionServiceImplimentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BankController {

    private final TransactionServiceImplimentation transactionServiceImplimentation;
    private final AccountServicesImplementation accountServicesImplementation;

    @PostMapping("/transfer")
    public ResponseEntity<String> sendMoney(@RequestBody SendMoneyRequest sendMoneyRequest) {
        String result = transactionServiceImplimentation.sendMoney(sendMoneyRequest);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/check-balance")
    public double checkBalance() {
        return accountServicesImplementation.checkBalance();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw (@RequestBody WithdrawRequest request) {
       return accountServicesImplementation.withdraw(request);
    }

}
