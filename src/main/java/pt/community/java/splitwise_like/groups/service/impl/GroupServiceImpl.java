package pt.community.java.splitwise_like.groups.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.repository.GroupRepository;
import pt.community.java.splitwise_like.groups.service.GroupService;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void addMember(Long groupId, Users user) {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.getUsers().add(user);
            groupRepository.save(group);
        });
    }

    @Override
    public void removeMember(Long groupId, Users user) {
        groupRepository.findById(groupId).ifPresent(group -> {
            group.getUsers().remove(user);
            groupRepository.save(group);
        });
    }

    @Override
    public void addExpense(Long groupId, Expense expense) {
        groupRepository.findById(groupId).ifPresent(group -> {
            // Set the group in the expense
            expense.setGroup(group);

            // Add the expense to the group's list of expenses
            group.getExpenses().add(expense);

            // Save the group with the updated list of expenses
            groupRepository.save(group);

            // Note: The expense itself should be saved by the expense service
        });
    }

    @Override
    public List<Expense> viewGroupExpense(Long groupId) {
        return groupRepository.findById(groupId)
                .map(Group::getExpenses)
                .orElse(new ArrayList<>());
    }
}
