
package pt.community.java.splitwise_like.expenses.splitstrategy;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("weightedSplitStrategy")
public class WeightedSplitStrategy implements SplitStrategy {

    public Map<Users, BigDecimal> calculateSplitFromMap(Map<Users, Integer> weights, BigDecimal totalAmount) {
        int totalWeight = weights.values().stream().mapToInt(i -> i).sum();

        Map<Users, BigDecimal> result = new HashMap<>();
        for (Map.Entry<Users, Integer> entry : weights.entrySet()) {
            BigDecimal share = totalAmount
                    .multiply(BigDecimal.valueOf(entry.getValue()))
                    .divide(BigDecimal.valueOf(totalWeight), 2, RoundingMode.HALF_UP);

            result.put(entry.getKey(), share);
        }
        return result;
    }

    @Override
    public Map<Users, BigDecimal> calculateSplit(BigDecimal totalAmount, List<Users> users) {
        throw new UnsupportedOperationException("Weighted split requires external map input.");
    }
}
