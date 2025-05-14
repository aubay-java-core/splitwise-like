package pt.community.java.splitwise_like.expenses.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.request.ExpenseRequest;
import pt.community.java.splitwise_like.expenses.response.ExpenseResponse;
import pt.community.java.splitwise_like.expenses.service.ExpenseService;
import pt.community.java.splitwise_like.groups.service.GroupService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final GroupService groupService;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(expense -> {
                    ExpenseResponse response = new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> responses = expenseService.findAll().stream()
                .map(expense -> new ExpenseResponse(expense.getExpenseId(), expense.getDescription(), expense.getAmount()))
                .toList();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        return expenseService.findById(id)
                .map(existingExpense -> {
                    existingExpense.setAmount(request.amount());
                    existingExpense.setDescription(request.description());

                    // Atualizar o grupo, se fornecido
                    if (request.groupId() != null) {
                        groupService.findById(request.groupId()).ifPresent(existingExpense::setGroup);
                    }

                    Expense updatedExpense = expenseService.updateExpense(existingExpense);
                    ExpenseResponse response = new ExpenseResponse(updatedExpense.getExpenseId(), updatedExpense.getDescription(), updatedExpense.getAmount());
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(expense -> {
                    expenseService.deleteExpense(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{expenseId}/shares")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Double>> calculateShares(@PathVariable Long expenseId) {
        return expenseService.findById(expenseId)
                .map(expense -> {
                    Map<Users, Double> shares = expenseService.calculateShares(expenseId);
                    // Convert Map<Users, Double> to Map<String, Double> using email as key
                    Map<String, Double> userShares = new HashMap<>();
                    shares.forEach((user, amount) -> userShares.put(user.getEmail(), amount));
                    return new ResponseEntity<>(userShares, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
