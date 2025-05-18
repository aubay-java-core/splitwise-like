package pt.community.java.splitwise_like.groups.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.request.ExpenseRequest;
import pt.community.java.splitwise_like.expenses.response.ExpenseResponse;
import pt.community.java.splitwise_like.expenses.service.ExpenseService;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.request.GroupRequest;
import pt.community.java.splitwise_like.groups.request.UserIdRequest;
import pt.community.java.splitwise_like.groups.response.GroupResponse;
import pt.community.java.splitwise_like.groups.service.GroupService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final ExpenseService expenseService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    //TODO Refator GroupResponse
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequest groupRequest) {
        Group group = new Group();
        group.setName(groupRequest.name());

        Set<Users> users = new HashSet<>();
        for (Long userId : groupRequest.userIds()) {
            userService.findById(userId).ifPresent(users::add);
        }
        group.setUsers(users);

        Group createdGroup = groupService.createGroup(group);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(group -> {
                    List<String> participants = group.getUsers().stream()
                            .map(Users::getEmail) // Ou use outro atributo, como nome
                            .toList();

                    List<ExpenseResponse> expenses = group.getExpenses().stream()
                            .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                            .toList();

                    GroupResponse response = new GroupResponse(group.getGroupId(), group.getName(), participants, expenses);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groupResponses = groupService.findAll().stream()
                .map(group -> {
                    List<String> participants = group.getUsers().stream()
                            .map(Users::getEmail)
                            .toList();

                    List<ExpenseResponse> expenses = group.getExpenses().stream()
                            .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                            .toList();

                    return new GroupResponse(group.getGroupId(), group.getName(), participants, expenses);
                })
                .toList();

        return new ResponseEntity<>(groupResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody GroupRequest groupRequest) {
        return groupService.findById(id)
                .map(existingGroup -> {
                    existingGroup.setName(groupRequest.name());

                    Set<Users> users = new HashSet<>();
                    for (Long userId : groupRequest.userIds()) {
                        userService.findById(userId).ifPresent(users::add);
                    }
                    existingGroup.setUsers(users);

                    Group updatedGroup = groupService.updateGroup(existingGroup);

                    List<String> participants = updatedGroup.getUsers().stream()
                            .map(Users::getEmail)
                            .toList();

                    List<ExpenseResponse> expenses = updatedGroup.getExpenses().stream()
                            .map(expense -> new ExpenseResponse(existingGroup.getGroupId(), expense.getDescription(), expense.getAmount()))
                            .toList();

                    GroupResponse response = new GroupResponse(updatedGroup.getGroupId(), updatedGroup.getName(), participants, expenses);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        return groupService.findById(id)
                .map(group -> {
                    groupService.deleteGroup(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{groupId}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addMember(@PathVariable Long groupId, @RequestBody UserIdRequest request) {
        return groupService.findById(groupId)
                .map(group -> {
                    userService.findById(request.userId()).ifPresent(user -> {
                        groupService.addMember(groupId, user);
                    });
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{groupId}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @RequestBody UserIdRequest request) {
        return groupService.findById(groupId)
                .map(group -> {
                    userService.findById(request.userId()).ifPresent(user -> {
                        groupService.removeMember(groupId, user);
                    });
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{groupId}/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addExpense(@PathVariable Long groupId, @RequestBody ExpenseRequest expenseRequest) {

            //TODO Implement Model Mapper
            Expense expense = new Expense();
            expense.setAmount(expenseRequest.amount());
            expense.setDescription(expenseRequest.description());
            expense.setPaidBy(userService.findById(expenseRequest.paidByUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            expense.setSplitMethod(expenseRequest.splitMethodEnum());

            // Add the expense to the group
            groupService.addExpense(groupId, expense);

            return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/{groupId}/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ExpenseResponse>> viewGroupExpenses(@PathVariable Long groupId) {
        return groupService.findById(groupId)
                .map(group -> {
                    List<ExpenseResponse> expenses = groupService.viewGroupExpense(groupId).stream()
                            .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                            .toList();
                    return new ResponseEntity<>(expenses, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
