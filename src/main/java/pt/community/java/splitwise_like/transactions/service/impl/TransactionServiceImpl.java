package pt.community.java.splitwise_like.transactions.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.model.SplitDetail;
import pt.community.java.splitwise_like.expenses.service.ExpenseService;
import pt.community.java.splitwise_like.expenses.service.SplitDetailService;
import pt.community.java.splitwise_like.transactions.model.Transaction;
import pt.community.java.splitwise_like.transactions.repository.TransactionRepository;
import pt.community.java.splitwise_like.transactions.request.TransactionRequest;
import pt.community.java.splitwise_like.transactions.service.TransactionService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final ExpenseService expenseService;
    private final SplitDetailService splitDetailService;


    @Override
    public Transaction registerTransaction(TransactionRequest request) {
        Users payer = userService.findById(request.fromUserId())
                .orElseThrow(() -> new RuntimeException("Payer not found."));

        Users receiver = userService.findById(request.toUserId())
                .orElseThrow(() -> new RuntimeException("Receiver not found."));

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero.");
        }

        if (payer.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("A user cannot send a transaction to themselves.");
        }

        Expense expense = null;

        if (request.expenseId() != null) {
            expense = expenseService.findById(request.expenseId())
                    .orElseThrow(() -> new RuntimeException("Expense not found."));

            BigDecimal totalDue = splitDetailService
                    .findByExpenseIdAndUserId(expense.getExpenseId(), payer.getId());

            BigDecimal totalPago = transactionRepository.sumAmountByExpenseIdAndFromUserId(expense.getExpenseId(), payer.getId());

            BigDecimal newTotal = totalPago.add(request.amount());

            if (newTotal.compareTo(totalDue) > 0) {
                throw new IllegalArgumentException("User is trying to pay more than he should.");
            }
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.amount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setExpense(expense);
        transaction.setFromUser(payer);
        transaction.setToUser(receiver);


        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findByFromUserIdOrToUserId(userId, userId);
    }

    @Override
    public List<Transaction> getTransactionsByExpense(Long expenseId) {
        return transactionRepository.findByExpenseExpenseId(expenseId);
    }
}
