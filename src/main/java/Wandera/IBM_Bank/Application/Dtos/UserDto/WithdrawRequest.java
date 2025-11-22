package Wandera.IBM_Bank.Application.Dtos.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawRequest {
    private  String agentNumber;
    private double amount;
}
