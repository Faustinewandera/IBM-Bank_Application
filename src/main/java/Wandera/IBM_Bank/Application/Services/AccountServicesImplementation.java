package Wandera.IBM_Bank.Application.Services;

import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountRequest;
import Wandera.IBM_Bank.Application.Dtos.AccountDto.AccountResponse;
import Wandera.IBM_Bank.Application.Dtos.AgentAccountDto.DepositRequest;
import Wandera.IBM_Bank.Application.Dtos.UserDto.WithdrawRequest;
import Wandera.IBM_Bank.Application.Entities.*;
import Wandera.IBM_Bank.Application.Mappers.AccountMapper;
import Wandera.IBM_Bank.Application.Repository.AccountRepository;
import Wandera.IBM_Bank.Application.Repository.AgentAccountRepository;
import Wandera.IBM_Bank.Application.Repository.TransactionHistoryRepository;
import Wandera.IBM_Bank.Application.Repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class AccountServicesImplementation {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AgentAccountRepository agentAccountRepository;
    private final EmailService emailService;
    private ReentrantLock lock = new ReentrantLock();


    List saveTransactionHistory = new LinkedList<>();
    List saveAccount = new LinkedList<>();

    //Method that generate account number
    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;

        //this generates accountNumber of 9 digit
        // do...while loop  help to generate unique account number
        do {
            accountNumber = String.format("%010d", random.nextInt(1_000_000_000));
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        return accountNumber;
    }

    public AccountResponse creatAccount(AccountRequest accountRequest) {

        // Ued to find the current logged in account
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccount() != null) {
            throw new RuntimeException("User already has an account");
        }

        var account = Account.builder()
                .username(accountRequest.getUsername())
                .accountNumber(generateAccountNumber())
                .balance(0.00)
                .user(accountRequest.getUser()) //this link the account to the user
                .build();

        //this save the account in the user table
        user.setAccount(account);

        saveAccount.add(accountRepository.save(account));
        var response = AccountMapper.toDto(account);

        ResponseEntity.status(HttpStatus.CREATED).body(response);
        return response;
    }

    public double checkBalance() {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find user from DB
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        //  Get the user’s account
        Account account = user.getAccount();
        if (account == null) {
            throw new RuntimeException("Account not found for user: " + username);
        }

        //  Return the balance
        return account.getBalance();
    }

    public String updateAccount(Long id, AccountRequest accountRequest) {

        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Account account = null;

        if (Objects.nonNull(account.getUsername()) && !"".equalsIgnoreCase(account.getUsername())) {
            account.setUsername(account.getUsername());
        }
        saveAccount.add(accountRepository.save(account));
        return "Your username updated successfully. " + account.getUsername();
    }


    public ResponseEntity<String> deposit(DepositRequest depositRequest, String agentNumber) {


        // 1. Get logged-in agent
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("No logged-in agent found");
        }
        String agentEmail = auth.getName();

        User agentUser = userRepository.findByEmail(agentEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in agent not found"));

        Account findAccountNumber=accountRepository.findByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(()->new RuntimeException("Account number not found"));

       double balance= findAccountNumber.getBalance();

        User receiverUser= findAccountNumber.getUser();
        String receiverUserEmail=receiverUser.getEmail();


        if (agentUser.getRole() != Role.AGENT) {
            throw new RuntimeException("Only agents can deposit to users");
        }


        AgentAccount agentAccount = agentUser.getAgentAccount();
        if (agentAccount == null) {
            throw new RuntimeException("Agent does not have a  account");
        }

        // 2. Check agent float balance
        double depositAmount = depositRequest.getAmount();
        if (agentAccount.getBalance() - 1000 < depositAmount) {
            throw new RuntimeException("Insufficient float balance in your agent account");
        }

        // 3. Get target user account
        Account userAccount = accountRepository.findByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("User account not found"));

        //Using the ReentrantLock to avoid multiple deposit to user account that can lead to negative balance

        lock.lock();
        try {

            // 4. Deduct from agent float balance
            agentAccount.setBalance(agentAccount.getBalance() - depositAmount);
            agentAccountRepository.save(agentAccount);


        // 5. Credit user account
        userAccount.setBalance(userAccount.getBalance() + depositAmount);
        accountRepository.save(userAccount);

            // Sends deposit email
            try {
                emailService.depositNotification( receiverUserEmail,depositRequest.getAmount(),agentNumber,balance);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        // 6. Save transaction history
        // Record transaction
        TransactionHistory transaction = TransactionHistory.builder()
                .senderAccount("DEPOSIT_BY_AGENT")
                .receiverAccount(depositRequest.getAccountNumber())
                .amount(depositAmount)
                .type("DEPOSIT")
                .timestamp(LocalDateTime.now())
                .account(userAccount)
                .timestamp(LocalDateTime.now())
                .build();
        saveTransactionHistory.add(transactionHistoryRepository.save(transaction));

        }
        finally{
            lock.unlock();
        }
        // 7. Return success
        return ResponseEntity.ok("Deposit successful. User new balance: " + userAccount.getBalance());

    }


    public ResponseEntity<String> withdraw(WithdrawRequest request) {
        // 1. Get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("");
        }
        String userEmail = auth.getName();

        //  Find user and their account
        User senderUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

       String receiverUser= senderUser.getEmail();

        lock.lock();

        try {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException(""));

            Account userAccount = user.getAccount();
            if (userAccount == null) {
                throw new RuntimeException("You do not have an account number. Create one");
            }


            // 2. Get agent account
            AgentAccount agentAccount = agentAccountRepository.findByAgentNumber(request.getAgentNumber())
                    .orElseThrow(() -> new RuntimeException("You entered an invalid agent number"));


//        // 3. Checking agent float balance
            double withdrawalAmount = getWithdrawalAmount(request, userAccount);

            // 5. Add withdrawal amount to agent  float balance
            agentAccount.setBalance(agentAccount.getBalance() + withdrawalAmount);
            agentAccountRepository.save(agentAccount);

            // 6. Deduct from user account
            userAccount.setBalance(userAccount.getBalance() - withdrawalAmount);
            accountRepository.save(userAccount);

           var bankBalance= userAccount.getBalance();

            // Sends withdrawal email
            try {
                emailService.withdrawNotification(receiverUser, request.getAmount(),bankBalance, request.getAgentNumber());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            // 7. Save transaction history
            TransactionHistory history = new TransactionHistory();
            history.setSenderAccount(userAccount.getAccountNumber());
            history.setReceiverAccount(agentAccount.getAgentNumber());
            history.setAmount(withdrawalAmount);
            history.setType("WITHDRAW_FROM_AGENT");
            history.setTimestamp(LocalDateTime.now());

            transactionHistoryRepository.save(history);



        // 8. Return success
        return ResponseEntity.ok(" Your Withdrawal is  successful. Your new balance is: " + userAccount.getBalance());
    }
        finally{
            lock.unlock();  //this used to release the lock. now safe
        }

    }

    private static double getWithdrawalAmount(WithdrawRequest request, Account userAccount) {
        double withdrawalAmount = request.getAmount();

        if (userAccount.getBalance() < withdrawalAmount) {
            throw new RuntimeException("You have  insufficient funds. Your balance is " + userAccount.getBalance());
        }
        return withdrawalAmount;
    }

}












