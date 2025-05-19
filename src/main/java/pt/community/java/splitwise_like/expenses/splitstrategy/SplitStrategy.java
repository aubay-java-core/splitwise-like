package pt.community.java.splitwise_like.expenses.splitstrategy;

import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SplitStrategy {

    Map<Users, BigDecimal> calculateSplit(BigDecimal totalAmount, List<Users> users);
}
