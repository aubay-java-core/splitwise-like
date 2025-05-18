package pt.community.java.splitwise_like.groups.facade;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.groups.mapper.GroupMapper;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.request.GroupRequest;
import pt.community.java.splitwise_like.groups.response.GroupResponse;
import pt.community.java.splitwise_like.groups.service.GroupService;
import pt.community.java.splitwise_like.users.model.Users;
import pt.community.java.splitwise_like.users.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GroupManagementFacade {
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;

    public GroupManagementFacade(
            GroupService groupService,
            UserService userService, GroupMapper groupMapper
    ) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
    }

    public GroupResponse createGroup(GroupRequest groupRequest) {
        Group group = new Group();
        group.setName(groupRequest.name());

        Set<Users> users = new HashSet<>();
        for (Long userId : groupRequest.userIds()) {
            userService.findById(userId).ifPresent(users::add);
        }
        group.setUsers(users);

        Group createdGroup = groupService.save(group);
        return new GroupResponse(createdGroup.getGroupId(), createdGroup.getName(), users.stream()
                .map(Users::getEmail).toList(), List.of());
    }

    public GroupResponse updateGroup(Long groupId, GroupRequest groupRequest) {
        Group existingGroup = groupService.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Atualizar os dados do grupo
        existingGroup.setName(groupRequest.name());

        // Buscar e adicionar os usuários ao grupo
        Set<Users> users = new HashSet<>();
        for (Long userId : groupRequest.userIds()) {
            userService.findById(userId).ifPresent(users::add);
        }
        existingGroup.setUsers(users);

        // Salvar o grupo atualizado
        Group updatedGroup = groupService.save(existingGroup);

        // Usar o mapper para transformar em GroupResponse
        return groupMapper.toResponse(updatedGroup);
    }

}