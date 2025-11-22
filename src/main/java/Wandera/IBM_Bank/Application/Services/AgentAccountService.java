package Wandera.IBM_Bank.Application.Services;


import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.AgentAccountRequest;
import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.AgentAccountResponse;
import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.InitialTopUpRequest;
import Wandera.IBM_Bank.Application.Entities.AgentAccount;
import Wandera.IBM_Bank.Application.Entities.Role;
import Wandera.IBM_Bank.Application.Entities.TransactionHistory;
import Wandera.IBM_Bank.Application.Entities.User;
import Wandera.IBM_Bank.Application.Mappers.AgentAccountMapper;
import Wandera.IBM_Bank.Application.Repository.AgentAccountRepository;
import Wandera.IBM_Bank.Application.Repository.TransactionHistoryRepository;
import Wandera.IBM_Bank.Application.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgentAccountService {

    private final AgentAccountRepository agentAccountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private  final UserRepository userRepository;

    public AgentAccountResponse createAgentAccount(AgentAccountRequest agentAccount, String idNumber) {



        User user = userRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new RuntimeException("ID Number is not registered."));

        if (user.getRole() != Role.AGENT) {
            throw new RuntimeException("User is not registered as an AGENT.");
        }

        if (user.getAgentAccount() != null) {
            throw new RuntimeException("This agent already has an agent account.");
        }

        // 1. Generate unique agent code
        String agentCode = generateAgentAccount();

        // 2. Create new agent account
        AgentAccount agent = new AgentAccount();

        agent.setPhoneNumber(agentAccount.getPhoneNumber());
        agent.setAgentNumber(generateAgentAccount());
        agent.setName(agentAccount.getName());
        agent.setIdNumber(agentAccount.getIdNumber());
        agent.setKraPin(agentAccount.getKraPin());
        agent.setUser(user);
        agent.setTransactions(agentAccount.getTransactions());
        agent.setBalance(0.0);

        // 3. Save to DB
        var saveAgentAccount = agentAccountRepository.save(agent);

        user.setAgentAccount(saveAgentAccount);

        return AgentAccountMapper.toDto(saveAgentAccount);


    }

    private String generateAgentAccount() {
        String agentAccount;

        do {
            agentAccount = String.valueOf((int) (Math.random() * 90000) + 10000);
            // Generates a 5-digit number e.g., 12345
        }
        while (agentAccountRepository.existsByAgentNumber(agentAccount));

        return agentAccount;
    }


    public ResponseEntity<String> topFloatBalanceByBank(InitialTopUpRequest initialTopUpRequest) {

        AgentAccount agentAccount= agentAccountRepository.findByAgentNumber(initialTopUpRequest.getAgentNumber())
                .orElseThrow(()->new RuntimeException("Agent provided is wrong. Please try again."));


        // 2. Validate the amount
        if (initialTopUpRequest.getAmount() <=15_000) {
            throw new RuntimeException("Amount must be 15000 and above");

        }

        // 3. Add money to agent's float balance
        double newBalance = agentAccount.getBalance() + initialTopUpRequest.getAmount();
        agentAccount.setBalance(newBalance);

        // 4. Save the updated agent account
        agentAccountRepository.save(agentAccount);

        // 5. Optional: Record the top-up in transaction history
        TransactionHistory history = new TransactionHistory();
        history.setSenderAccount("Bank Account");
        history.setReceiverAccount(initialTopUpRequest.getAgentNumber());
        history.setAmount(initialTopUpRequest.getAmount());
        history.setType("FLOAT_TOPUP");
        history.setAgent(agentAccount);
        history.setTimestamp(LocalDateTime.now());

        transactionHistoryRepository.save(history);

        // 6. Return success response
        return ResponseEntity.ok("Float balance updated successfully. New balance: " + newBalance);
    }

}

