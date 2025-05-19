package pt.community.java.splitwise_like.expenses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
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
public class ExpenseServiceImpl extends AbstractCrudService<Expense,Long> implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    protected JpaRepository<Expense, Long> getRepository() {
        return expenseRepository;
    }


    @Override
    public Map<Users, Double> calculateShares(Long expenseId) {
        Map<Users, Double> shares = new HashMap<>();

        expenseRepository.findById(expenseId).ifPresent(expense -> {
            if (expense.getGroup() != null) {
                int participantCount = expense.getGroup().getUsers().size();

                if (participantCount > 0) {
                    double shareAmount = expense.getAmount().doubleValue() / participantCount;
                    expense.getGroup().getUsers().forEach(participant -> 
                        shares.put(participant, shareAmount)
                    );
                }
            }
        });

        return shares;
    }
}
