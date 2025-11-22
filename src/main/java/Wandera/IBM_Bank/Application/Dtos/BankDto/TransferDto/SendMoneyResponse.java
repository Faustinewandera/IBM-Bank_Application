package Wandera.IBM_Bank.Application.Dtos.BankDto.TransferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMoneyResponse {
    private double amount;
    private String accountNumber;
}
