package pt.community.java.splitwise_like.expenses.request;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public record ExpenseRequest(
    BigDecimal amount,
    String description,
    Set<Long> participantIds
) {
    public ExpenseRequest {
        if (participantIds == null) {
            participantIds = new HashSet<>();
        }
    }

    public ExpenseRequest(BigDecimal amount, String description) {
        this(amount, description, new HashSet<>());
    }
}