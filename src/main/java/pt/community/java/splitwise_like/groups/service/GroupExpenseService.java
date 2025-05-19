package pt.community.java.splitwise_like.groups.service;

import pt.community.java.splitwise_like.expenses.model.Expense;

import java.util.List;

public interface GroupExpenseService {
    void addExpense(Long groupId, Expense expense);
    List<Expense> viewGroupExpense(Long groupId);
}
