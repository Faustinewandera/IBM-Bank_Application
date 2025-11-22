package Wandera.IBM_Bank.Application.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, updatable = false)
    @NotBlank(message = "Id number required")
    private String idNumber;
    @Column(unique = true, updatable = false)
    @NotBlank(message = "Kra pin required")
    private String kraPin;
    private String agentNumber;
    @Column(unique = true)
    private String phoneNumber;
    private double balance;


    // Agent → TransactionHistory
    @OneToMany(mappedBy = "agent")
    private List<TransactionHistory> transactions;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
