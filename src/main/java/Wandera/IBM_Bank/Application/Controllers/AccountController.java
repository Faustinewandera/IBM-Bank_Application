package Wandera.IBM_Bank.Application.Controllers;

import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountRequest;
import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountResponse;
import Wandera.IBM_Bank.Application.Services.AccountServicesImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountServicesImplementation accountServicesImplementation;

    @PostMapping("/createAccount")
    public ResponseEntity<AccountResponse> creatAccount(@RequestBody AccountRequest accountRequest){
        return ResponseEntity.ok(accountServicesImplementation.creatAccount(accountRequest));
    }

    @PutMapping("/updateUsername/{id}")
    public String updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest){
        return accountServicesImplementation.updateAccount(id,accountRequest);

    }

}
