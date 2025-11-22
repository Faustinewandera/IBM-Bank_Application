package Wandera.IBM_Bank.Application.Services;


import Wandera.IBM_Bank.Application.Dtos.BankDto.TransferDto.SendMoneyRequest;
import Wandera.IBM_Bank.Application.Dtos.TransactionDto.TransactionResponse;
import Wandera.IBM_Bank.Application.Entities.Account;
import Wandera.IBM_Bank.Application.Entities.TransactionHistory;
import Wandera.IBM_Bank.Application.Entities.User;
import Wandera.IBM_Bank.Application.Mappers.TransactionMapper;
import Wandera.IBM_Bank.Application.Repository.AccountRepository;
import Wandera.IBM_Bank.Application.Repository.TransactionHistoryRepository;
import Wandera.IBM_Bank.Application.Repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImplimentation {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final EmailService emailService;


    public String sendMoney(SendMoneyRequest sendMoneyRequest) {

            // Getting logged-in user email
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            //  Find user and their account
            User senderUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Account sender = senderUser.getAccount();


        // Find receiver account
            Account receiver = accountRepository.findByAccountNumber(sendMoneyRequest.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Receiver account not found"));


        // Get email
        String receiverEmail = senderUser.getEmail();
        String receiverName = senderUser.getLastName();



        //  Check sufficient balance
            if (sender.getBalance() < sendMoneyRequest.getAmount()) {
                throw new RuntimeException("Insufficient balance");
            }


            //  Transferring
            sender.setBalance(sender.getBalance() - sendMoneyRequest.getAmount());
            receiver.setBalance(receiver.getBalance() + sendMoneyRequest.getAmount());

           var updatedBalance= accountRepository.save(sender);
            accountRepository.save(receiver);

        double getBalance= updatedBalance.getBalance();

        // Sends transaction notification
        try {
            emailService.transferNotification( receiverEmail, receiverName, getBalance, senderUser.getFirstName(), sendMoneyRequest.getAmount(),sendMoneyRequest.getAccountNumber());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //finds the account balance
        double balance=receiver.getBalance();

        //This finds user of attached account
        User findsUserOfAccount= receiver.getUser();

        String receiverUserEmail=   findsUserOfAccount.getEmail();
        String receiverUserName = findsUserOfAccount.getFirstName();



        // Sends received money email
        try {
            emailService.receivedMoneyNotification( receiverUserEmail,receiverUserName,sendMoneyRequest.getAmount(),balance);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


            // Record transaction
        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setSenderAccount(sender.getAccountNumber());
        transactionHistory.setReceiverAccount(receiver.getAccountNumber());
        transactionHistory.setAmount(sendMoneyRequest.getAmount());
        transactionHistory.setType("TRANSFER");
        transactionHistory.setAccount(sender);
        transactionHistory.setTimestamp(LocalDateTime.now());

         transactionHistoryRepository.save(transactionHistory);

            return "Transfer successful: " + sendMoneyRequest.getAmount()
                    + " from " + sender.getAccountNumber()
                    + " to " + receiver.getAccountNumber();
        }


    public List<TransactionResponse> getAllTransactionHistory(String accountNumber) {

//      var transactionHistory=   transactionHistoryRepository.findByAccountNumber(accountNumber)
//                 .orElseThrow(()->new RuntimeException("Account Number does not  found"))

        return transactionHistoryRepository.findAll()
                .stream()
                .map(TransactionMapper::toDto)
                .toList();

    }

    public List<TransactionResponse> getByAccountNumber(String accountNumber) {

        // 1. Get all transactions for the account number
        List<TransactionHistory> transactions =
                transactionHistoryRepository.findByAccount_AccountNumber(accountNumber);

        // 2. Convert Entity List → DTO List
        return transactions
                .stream()
                .map(TransactionMapper::toDto)
                .toList();

    }


}
