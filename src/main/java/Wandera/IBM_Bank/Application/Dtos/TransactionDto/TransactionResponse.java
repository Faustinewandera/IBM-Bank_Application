package Wandera.IBM_Bank.Application.Dtos.TransactionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private String senderAccount;
    private String receiverAccount;
    private Double amount;
    private String type;
    private LocalDateTime timestamp;

}
