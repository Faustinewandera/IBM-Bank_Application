package Wandera.IBM_Bank.Application.Dtos.AgentAccountDto;

import Wandera.IBM_Bank.Application.Entities.TransactionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentAccountRequest {
    private String name;
    private String idNumber;
    private String phoneNumber;
    private String kraPin;
    private List<TransactionHistory> transactions;
}
