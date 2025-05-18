package pt.community.java.splitwise_like.groups.mapper;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.request.ExpenseRequest;
import pt.community.java.splitwise_like.users.model.Users;

@Component
public class ExpenseMapper {

    public Expense toExpense(ExpenseRequest expenseRequest, Users paidByUser) {
        Expense expense = new Expense();
        expense.setAmount(expenseRequest.amount());
        expense.setDescription(expenseRequest.description());
        expense.setPaidBy(paidByUser);
        expense.setSplitMethod(expenseRequest.splitMethodEnum());
        return expense;
    }

}
