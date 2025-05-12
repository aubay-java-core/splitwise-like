package pt.community.java.splitwise_like.groups.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.request.GroupRequest;
import pt.community.java.splitwise_like.groups.request.UserIdRequest;
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

    @PostMapping
    @PreAuthorize("hasRole('USER')")
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
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(group -> new ResponseEntity<>(group, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupRequest groupRequest) {
        return groupService.findById(id)
                .map(existingGroup -> {
                    existingGroup.setName(groupRequest.name());

                    Set<Users> users = new HashSet<>();
                    for (Long userId : groupRequest.userIds()) {
                        userService.findById(userId).ifPresent(users::add);
                    }
                    existingGroup.setUsers(users);

                    Group updatedGroup = groupService.updateGroup(existingGroup);
                    return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
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
    public ResponseEntity<Void> addExpense(@PathVariable Long groupId, @RequestBody Expense expense) {
        return groupService.findById(groupId)
                .map(group -> {
                    groupService.addExpense(groupId, expense);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{groupId}/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Expense>> viewGroupExpenses(@PathVariable Long groupId) {
        return groupService.findById(groupId)
                .map(group -> {
                    List<Expense> expenses = groupService.viewGroupExpense(groupId);
                    return new ResponseEntity<>(expenses, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
