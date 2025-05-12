package pt.community.java.splitwise_like.expenses.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.expenses.request.ExpenseRequest;
import pt.community.java.splitwise_like.expenses.service.ExpenseService;
import pt.community.java.splitwise_like.groups.request.UserIdRequest;
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

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setAmount(request.amount());
        expense.setDescription(request.description());

        // Note: Participants should be added separately using the addParticipant endpoint

        Expense createdExpense = expenseService.createExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(expense -> new ResponseEntity<>(expense, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.findAll();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        return expenseService.findById(id)
                .map(existingExpense -> {
                    existingExpense.setAmount(request.amount());
                    existingExpense.setDescription(request.description());

                    // Note: Participants should be updated separately using the participant endpoints

                    Expense updatedExpense = expenseService.updateExpense(existingExpense);
                    return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
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

    @PostMapping("/{expenseId}/participants")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addParticipant(@PathVariable Long expenseId, @RequestBody UserIdRequest request) {
        return expenseService.findById(expenseId)
                .map(expense -> {
                    return userService.findById(request.userId())
                            .map(user -> {
                                expenseService.addParticipant(expenseId, user);
                                return new ResponseEntity<Void>(HttpStatus.OK);
                            })
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{expenseId}/participants")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long expenseId, @RequestBody UserIdRequest request) {
        return expenseService.findById(expenseId)
                .map(expense -> {
                    return userService.findById(request.userId())
                            .map(user -> {
                                expenseService.removeParticipant(expenseId, user);
                                return new ResponseEntity<Void>(HttpStatus.OK);
                            })
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
