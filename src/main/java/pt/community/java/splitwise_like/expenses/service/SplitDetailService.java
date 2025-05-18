package pt.community.java.splitwise_like.expenses.service;

import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.model.SplitDetail;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SplitDetailService {

    Map<Users, BigDecimal> createSplitDetail(Expense expense);

    BigDecimal findByExpenseIdAndUserId(Long expenseId, Long userId);
}
