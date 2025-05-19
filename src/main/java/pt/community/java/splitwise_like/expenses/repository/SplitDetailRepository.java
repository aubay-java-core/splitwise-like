package pt.community.java.splitwise_like.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.expenses.model.SplitDetail;

import java.util.Optional;

public interface SplitDetailRepository extends JpaRepository<SplitDetail, Long> {

    Optional<SplitDetail> findByExpenseExpenseIdAndUserId(Long expenseId, Long userId);
}