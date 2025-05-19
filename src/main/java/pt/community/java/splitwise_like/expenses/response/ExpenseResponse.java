package pt.community.java.splitwise_like.expenses.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private String description;
    private BigDecimal amount;
}