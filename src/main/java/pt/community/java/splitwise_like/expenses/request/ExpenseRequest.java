package pt.community.java.splitwise_like.expenses.request;

import java.math.BigDecimal;

public record ExpenseRequest(
    BigDecimal amount,
    String description,
    Long groupId
) {
    public ExpenseRequest(BigDecimal amount, String description) {
        this(amount, description, null);
    }
}
