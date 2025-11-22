package Wandera.IBM_Bank.Application.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "First_Name")
    private String firstName;
    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Id_Number", unique = true)
    @NotBlank(message = "Id number required")
    private String idNumber;

    @Column(name = "Email", unique = true)
    @NotBlank(message = "Email field required")
    private String email;

    @Column(name = "Password")
    @NotBlank(message = "Password cannot be null")
    private String password;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private AgentAccount agentAccount;


}
