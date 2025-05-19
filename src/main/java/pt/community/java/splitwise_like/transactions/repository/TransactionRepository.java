package pt.community.java.splitwise_like.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.community.java.splitwise_like.transactions.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromUserIdOrToUserId(Long fromUserId, Long toUserId);
    List<Transaction> findByExpenseExpenseId(Long expenseId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.expense.expenseId = :expenseId AND t.fromUser.id = :userId")
    BigDecimal sumAmountByExpenseIdAndFromUserId(Long expenseId, Long userId);}