package pt.community.java.splitwise_like.groups.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.service.SplitDetailService;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.repository.GroupRepository;
import pt.community.java.splitwise_like.groups.service.GroupService;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends AbstractCrudService<Group, Long> implements GroupService {

    private final GroupRepository groupRepository;
    private  final SplitDetailService splitDetailService;

    @Override
    public Group createGroup(Group group) {
        return this.save(group);
    }

    @Override
    protected JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }


    @Override
    public void addMember(Long groupId, Users user) {
        this.findById(groupId).ifPresent(group -> {
            group.getUsers().add(user);
            this.save(group);
        });
    }

    @Override
    public void removeMember(Long groupId, Users user) {
        this.findById(groupId).ifPresent(group -> {
            group.getUsers().remove(user);
            this.save(group);
        });
    }

    @Override
    public void addExpense(Long groupId, Expense expense) {
        this.findById(groupId).ifPresent(group -> {
            // Set the group in the expense
            expense.setGroup(group);
            expense.setSplits(splitDetailService.createSplitDetail(expense));

            // Add the expense to the group's list of expenses
            group.getExpenses().add(expense);

            // Save the group with the updated list of expenses
            this.save(group);

            // Note: The expense itself should be saved by the expense service
        });
    }

    @Override
    public List<Expense> viewGroupExpense(Long groupId) {
        return this.findById(groupId)
                .map(Group::getExpenses)
                .orElse(new ArrayList<>());
    }
}
