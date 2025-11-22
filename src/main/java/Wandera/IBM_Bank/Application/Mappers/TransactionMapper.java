package Wandera.IBM_Bank.Application.Mappers;

import Wandera.IBM_Bank.Application.Dtos.TransactionDto.TransactionResponse;
import Wandera.IBM_Bank.Application.Entities.TransactionHistory;

public class TransactionMapper {

    public static TransactionResponse toDto(TransactionHistory transactionHistory) {

        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setType(transactionHistory.getType());
        transactionResponse.setReceiverAccount(transactionHistory.getReceiverAccount());
        transactionResponse.setSenderAccount(transactionHistory.getSenderAccount());
        transactionResponse.setAmount(transactionHistory.getAmount());
        transactionResponse.setTimestamp(transactionHistory.getTimestamp());

        return transactionResponse;
    }
}
