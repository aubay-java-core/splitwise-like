package pt.community.java.splitwise_like.expenses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.expenses.enums.SplitMethodEnum;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.model.SplitDetail;
import pt.community.java.splitwise_like.expenses.repository.ExpenseRepository;
import pt.community.java.splitwise_like.expenses.repository.SplitDetailRepository;
import pt.community.java.splitwise_like.expenses.service.SplitDetailService;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SlipDetailServiceImpl implements SplitDetailService {

    private final ExpenseRepository expenseRepository;

    /**
     * Creates split details based on the selected split method.
     *
     * @param expense A despesa que será dividida
     * @return Lista de detalhes da divisão (SplitDetail)
     */
    @Override
    public Map<Users, BigDecimal> createSplitDetail(Expense expense) {
        validateExpense(expense);

        Map<Users, BigDecimal> splitDetails;



        SplitMethodEnum splitMethod = Optional.ofNullable(expense.getSplitMethod())
                .orElseThrow(() -> new IllegalArgumentException("O método de divisão não pode ser nulo!"));

        switch (splitMethod) {
            case EQUAL -> splitDetails = createEqualSplitDetails(expense);
            // Implement other types of division here if necessary.
            //  Best way is to refactor with strategy
            default -> throw new UnsupportedOperationException("Método de divisão não suportado: " + splitMethod);
        }

        return splitDetails;
    }

    /**
     * @param expenseId
     * @param userId
     * @return
     */
    @Override
    public BigDecimal findByExpenseIdAndUserId(Long expenseId, Long userId) {
        return expenseRepository.findByExpenseExpenseIdAndUserId(expenseId, userId);
    }

    /**
     * Divide the amounts equally among the group participants.
     *
     * @param expense A despesa que será dividida
     * @return Lista de detalhes de divisão igualitária
     */
    private Map<Users, BigDecimal> createEqualSplitDetails(Expense expense) {
       var participants = expense.getGroup().getUsers();
        int participantCount = participants != null ? participants.size() : 0;

        if (participantCount == 0) {
            throw new IllegalArgumentException("O grupo deve conter pelo menos um participante.");
        }

        // Calcula o valor a ser dividido por participante
        BigDecimal shareAmount = expense.getAmount()
                .divide(BigDecimal.valueOf(participantCount), 2, RoundingMode.HALF_UP);

        Map<Users, BigDecimal> splits = new HashMap<>();

        List<SplitDetail> splitDetails = new ArrayList<>();
        participants.forEach(participant -> {


             splits.put(participant, shareAmount);

        });

        return splits;
    }

    /**
     * Validates whether the `Expense` object is correctly configured.
     *
     * @param expense The expense instance to be validated
     */
    private void validateExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("The expense cannot be zero.");
        }

        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The expense amount must be greater than zero.");
        }

        if (expense.getGroup() == null || expense.getGroup().getUsers() == null) {
            throw new IllegalArgumentException("The expense must be associated with a group with users.");
        }
    }
}