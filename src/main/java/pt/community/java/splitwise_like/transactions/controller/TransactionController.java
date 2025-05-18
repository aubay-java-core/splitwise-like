package pt.community.java.splitwise_like.transactions.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.transactions.model.Transaction;
import pt.community.java.splitwise_like.transactions.request.TransactionRequest;
import pt.community.java.splitwise_like.transactions.response.TransactionResponse;
import pt.community.java.splitwise_like.transactions.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> registerTransaction(@RequestBody TransactionRequest request) {

        TransactionResponse response = convertToTransactionResponse(transactionService.registerTransaction(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(@PathVariable Long userId) {

        var transcationList = transactionService.getTransactionsByUser(userId);

        return ResponseEntity.ok(transcationList.stream().map(this::convertToTransactionResponse).toList());
    }



    private TransactionResponse convertToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();

        response.setReceiver(transaction.getToUser().getName());
        response.setDescription(transaction.getExpense().getDescription());
        response.setAmount(transaction.getAmount());
        response.setDate(transaction.getCreatedAt());

        return response;
    }


}
