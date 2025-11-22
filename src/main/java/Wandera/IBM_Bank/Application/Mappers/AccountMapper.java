package Wandera.IBM_Bank.Application.Mappers;


import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountRequest;
import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountResponse;
import Wandera.IBM_Bank.Application.Entities.Account;

public class AccountMapper {

    public static Account toEntity(AccountRequest accountRequest){
        Account account=new Account();
        account.setUsername(accountRequest.getUsername());

        return account;
    }
    public static AccountResponse toDto(Account account){
        AccountResponse accountResponse=new AccountResponse();

        accountResponse.setId(account.getId());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setUsername(account.getUsername());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setUsername(account.getUsername());

        return accountResponse;

    }
}
