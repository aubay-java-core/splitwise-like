package pt.community.java.splitwise_like.groups.service;

import pt.community.java.splitwise_like.users.model.Users;

public interface GroupMemberService {
    void addMember(Long groupId, Users user);
    void removeMember(Long groupId, Users user);

}
