package Wandera.IBM_Bank.Application.Repository;

import Wandera.IBM_Bank.Application.Entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,Long> {

    List<TransactionHistory> findByAccount_AccountNumber(String accountNumber);
}
