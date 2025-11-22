package Wandera.IBM_Bank.Application.Dtos.AgentAccountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentAccountResponse {
    private Long id;
    private String name;
    private String agentNumber;
    private String phoneNumber;
}
