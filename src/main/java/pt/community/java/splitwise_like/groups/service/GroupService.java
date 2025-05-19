package pt.community.java.splitwise_like.groups.service;

import pt.community.java.splitwise_like.groups.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    // CRUD operations
    Optional<Group> findById(Long id);
    List<Group> findAll();
    Group save(Group existingGroup);
    void delete(Long id);
}
