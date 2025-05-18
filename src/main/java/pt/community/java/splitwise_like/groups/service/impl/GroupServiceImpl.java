package pt.community.java.splitwise_like.groups.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pt.community.java.splitwise_like.commons.AbstractCrudService;
import pt.community.java.splitwise_like.expenses.service.SplitDetailService;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.groups.repository.GroupRepository;
import pt.community.java.splitwise_like.groups.service.GroupService;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends AbstractCrudService<Group, Long> implements GroupService {

    private final GroupRepository groupRepository;
    private  final SplitDetailService splitDetailService;



    @Override
    protected JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }



}
