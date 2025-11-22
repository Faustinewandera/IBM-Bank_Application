package Wandera.IBM_Bank.Application.Repository;

import Wandera.IBM_Bank.Application.Entities.AgentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentAccountRepository extends JpaRepository<AgentAccount, Long> {

    boolean existsByAgentNumber(String agentNumber);

    Optional<AgentAccount> findByAgentNumber(String agentNumber);
}
