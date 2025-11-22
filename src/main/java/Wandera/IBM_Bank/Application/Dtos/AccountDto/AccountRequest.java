package Wandera.IBM_Bank.Application.Dtos.AccountDto;

import Wandera.IBM_Bank.Application.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    private User user;
    private String username;


}
