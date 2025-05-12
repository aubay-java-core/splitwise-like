package pt.community.java.splitwise_like.expenses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.repository.ExpenseRepository;
import pt.community.java.splitwise_like.expenses.service.ExpenseService;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public void addParticipant(Long expenseId, Users user) {
        expenseRepository.findById(expenseId).ifPresent(expense -> {
            expense.getParticipants().add(user);
            expenseRepository.save(expense);
        });
    }

    @Override
    public void removeParticipant(Long expenseId, Users user) {
        expenseRepository.findById(expenseId).ifPresent(expense -> {
            expense.getParticipants().remove(user);
            expenseRepository.save(expense);
        });
    }

    @Override
    public Map<Users, Double> calculateShares(Long expenseId) {
        Map<Users, Double> shares = new HashMap<>();
        
        expenseRepository.findById(expenseId).ifPresent(expense -> {
            int participantCount = expense.getParticipants().size();
            if (participantCount > 0) {
                double shareAmount = expense.getAmount().doubleValue() / participantCount;
                expense.getParticipants().forEach(participant -> 
                    shares.put(participant, shareAmount)
                );
            }
        });
        
        return shares;
    }
}