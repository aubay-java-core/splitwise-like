package pt.community.java.splitwise_like.groups.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.community.java.splitwise_like.groups.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // Inherits basic CRUD operations from JpaRepository
    // Can add custom query methods if needed
}