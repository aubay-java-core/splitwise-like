package pt.community.java.splitwise_like.groups.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.repository.GroupRepository;
import pt.community.java.splitwise_like.groups.service.GroupMemberService;
import pt.community.java.splitwise_like.users.model.Users;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl extends AbstractCrudService<Group, Long> implements GroupMemberService {

    private final GroupRepository groupRepository;

    @Override
    protected JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }

    @Override
    public void addMember(Long groupId, Users user) {
        Group group = this.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        group.getUsers().add(user);
        this.save(group);
    }

    @Override
    public void removeMember(Long groupId, Users user) {
        Group group = this.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        group.getUsers().remove(user);
        this.save(group);
    }



}
