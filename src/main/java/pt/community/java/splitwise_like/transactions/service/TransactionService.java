package pt.community.java.splitwise_like.transactions.service;

import pt.community.java.splitwise_like.transactions.model.Transaction;
import pt.community.java.splitwise_like.transactions.request.TransactionRequest;

import java.util.List;

public interface TransactionService {
    Transaction registerTransaction(TransactionRequest request);
    List<Transaction> getTransactionsByUser(Long userId);
    List<Transaction> getTransactionsByExpense(Long expenseId);
}