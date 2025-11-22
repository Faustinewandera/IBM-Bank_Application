package Wandera.IBM_Bank.Application.Mappers;


import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.AgentAccountResponse;
import Wandera.IBM_Bank.Application.Entities.AgentAccount;

public class AgentAccountMapper {

    public static AgentAccountResponse toDto(AgentAccount agentAccount) {
        AgentAccountResponse agentAccountResponse = new AgentAccountResponse();

        agentAccountResponse.setId(agentAccount.getId());
        agentAccountResponse.setName(agentAccount.getName());
        agentAccountResponse.setAgentNumber(agentAccount.getAgentNumber());
        agentAccountResponse.setPhoneNumber(agentAccount.getPhoneNumber());

        return agentAccountResponse;
    }

}
