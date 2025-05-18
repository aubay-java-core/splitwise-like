package pt.community.java.splitwise_like.expenses.request;

import pt.community.java.splitwise_like.expenses.enums.SplitMethodEnum;

import java.math.BigDecimal;

public record ExpenseRequest(
    BigDecimal amount,
    String description,
    Long groupId,
    Long paidByUserId,
    SplitMethodEnum splitMethodEnum
) {
    public ExpenseRequest(BigDecimal amount, String description) {
        this(amount, description, null, null, null);
    }
}
