package pt.community.java.splitwise_like.groups.request;

import java.util.HashSet;
import java.util.Set;

public record GroupRequest(
    String name,
    Set<Long> userIds
) {
    public GroupRequest {
        if (userIds == null) {
            userIds = new HashSet<>();
        }
    }

    public GroupRequest(String name) {
        this(name, new HashSet<>());
    }
}
