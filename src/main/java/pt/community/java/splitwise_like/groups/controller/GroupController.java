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
import pt.community.java.splitwise_like.groups.facade.GroupManagementFacade;
import pt.community.java.splitwise_like.groups.mapper.ExpenseMapper;
import pt.community.java.splitwise_like.groups.mapper.GroupMapper;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.request.GroupRequest;
import pt.community.java.splitwise_like.groups.request.UserIdRequest;
import pt.community.java.splitwise_like.groups.response.GroupResponse;
import pt.community.java.splitwise_like.groups.service.GroupExpenseService;
import pt.community.java.splitwise_like.groups.service.GroupMemberService;
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
    private final GroupMemberService groupMemberService;
    private final GroupExpenseService groupExpenseService;
    private final UserService userService;
    private final GroupMapper groupMapper;
    private final ExpenseMapper expenseMapper;
    private final GroupManagementFacade groupManagementFacade;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest groupRequest) {
        GroupResponse response = groupManagementFacade.createGroup(groupRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
            .map(groupMapper::toResponse)
            .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groupResponses = groupService.findAll().stream()
                .map(groupMapper::toResponse)
                .toList();

        if (groupResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(groupResponses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody GroupRequest groupRequest) {
        GroupResponse response = groupManagementFacade.updateGroup(id, groupRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{groupId}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addMember(@PathVariable Long groupId, @RequestBody UserIdRequest request) {

        userService.findById(request.userId()).ifPresent(user -> {
            groupMemberService.addMember(groupId, user);
        });

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/members")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @RequestBody UserIdRequest request) {

        userService.findById(request.userId()).ifPresent(user -> {
            groupMemberService.removeMember(groupId, user);
        });

        return  new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/{groupId}/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addExpense(@PathVariable Long groupId, @RequestBody ExpenseRequest expenseRequest) {
       var user =  userService.findById(expenseRequest.paidByUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Expense expense = expenseMapper.toExpense(expenseRequest,user);

        groupExpenseService.addExpense(groupId, expense);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/{groupId}/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ExpenseResponse>> viewGroupExpenses(@PathVariable Long groupId) {
        return groupService.findById(groupId)
                .map(group -> {
                    List<ExpenseResponse> expenses = groupExpenseService.viewGroupExpense(groupId).stream()
                            .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                            .toList();
                    return new ResponseEntity<>(expenses, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}