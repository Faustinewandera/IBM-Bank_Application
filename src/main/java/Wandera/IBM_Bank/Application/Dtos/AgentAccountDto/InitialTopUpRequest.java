package Wandera.IBM_Bank.Application.Dtos.AgentAccountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitialTopUpRequest {
    private String agentNumber;
    private double amount;
}
