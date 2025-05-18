package pt.community.java.splitwise_like.groups.mapper;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.expenses.response.ExpenseResponse;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.response.GroupResponse;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.List;

@Component
public class GroupMapper {

    // TODO Why not use ModelMapper?
    public GroupResponse toResponse(Group group) {
        List<String> participants = group.getUsers().stream()
                .map(Users::getEmail)
                .toList();

        List<ExpenseResponse> expenses = group.getExpenses().stream()
                .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                .toList();

        return new GroupResponse(group.getGroupId(), group.getName(), participants, expenses);
    }
}
