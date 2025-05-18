package pt.community.java.splitwise_like.transactions.request;

import java.math.BigDecimal;

public record TransactionRequest(
        Long fromUserId,
        Long toUserId,
        BigDecimal amount,
        Long expenseId
) {}