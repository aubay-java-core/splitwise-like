package pt.community.java.splitwise_like.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.model.SplitDetail;

import java.math.BigDecimal;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    @Query(value = "SELECT SUM(amount) FROM expense_splits WHERE expense_id = :expenseId and user_id = :userId ", nativeQuery = true)
    BigDecimal findByExpenseExpenseIdAndUserId(Long expenseId, Long userId);

}