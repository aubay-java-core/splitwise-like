package pt.community.java.splitwise_like.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.expenses.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Inherits basic CRUD operations from JpaRepository
    // Can add custom query methods if needed
}