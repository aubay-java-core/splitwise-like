package pt.community.java.splitwise_like.groups.service;

import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.users.model.Users;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    // CRUD operations
    Group createGroup(Group group);
    Optional<Group> findById(Long id);
    List<Group> findAll();
    Group save(Group existingGroup);
    void delete(Long id);

    // Group member operations
    void addMember(Long groupId, Users user);
    void removeMember(Long groupId, Users user);

    // Group expense operations
    void addExpense(Long groupId, Expense expense);
    List<Expense> viewGroupExpense(Long groupId);

}
