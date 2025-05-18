package pt.community.java.splitwise_like.expenses.service;

import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseService {
    // CRUD operations
    Optional<Expense> findById(Long id);
    List<Expense> findAll();
    Expense save(Expense existingExpense);
    void delete(Long id);

    // Share calculation
    Map<Users, Double> calculateShares(Long expenseId);
}
