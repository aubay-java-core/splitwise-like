package pt.community.java.splitwise_like.expenses.request;

import pt.community.java.splitwise_like.expenses.enums.SplitMethodEnum;

import java.math.BigDecimal;
import java.util.Map;

public record ExpenseRequest(
    BigDecimal amount,
    String description,
    Long groupId,
    Long paidByUserId,
    SplitMethodEnum splitMethodEnum,
    Map<Long, BigDecimal> exactSplit,
    Map<Long, Double> percentageSplit,
    Map<Long, Integer> weightSplit
) {

}
