package pt.community.java.splitwise_like.groups.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.repository.GroupRepository;
import pt.community.java.splitwise_like.groups.service.GroupExpenseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupExpenseServiceImpl  extends AbstractCrudService<Group, Long> implements GroupExpenseService {

    private final GroupRepository groupRepository;

    @Override
    protected JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }

    @Override
    public void addExpense(Long groupId, Expense expense) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        group.getExpenses().add(expense);
        groupRepository.save(group);
    }

    @Override
    public List<Expense> viewGroupExpense(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        return group.getExpenses();
    }

}
