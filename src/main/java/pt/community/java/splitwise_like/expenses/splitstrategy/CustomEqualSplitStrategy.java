package pt.community.java.splitwise_like.expenses.splitstrategy;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("equalSplitStrategy")
public class CustomEqualSplitStrategy implements SplitStrategy {


    @Override
    public Map<Users, BigDecimal> calculateSplit(BigDecimal totalAmount, List<Users> users) {
        Map<Users, BigDecimal> result = new HashMap<>();
        if (users.isEmpty()) return result;
        BigDecimal share = totalAmount.divide(BigDecimal.valueOf(users.size()), 2, RoundingMode.HALF_UP);

        for (Users user : users) {
            result.put(user, share);
        }
        return result;
    }
}
