package Wandera.IBM_Bank.Application.Dtos.BankDto.DepositDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositResponse {
    private double amount;
    private String accountNumber;
}
