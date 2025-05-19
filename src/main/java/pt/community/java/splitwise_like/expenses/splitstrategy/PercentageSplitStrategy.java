
package pt.community.java.splitwise_like.expenses.splitstrategy;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("percentageSplitStrategy")
public class PercentageSplitStrategy implements SplitStrategy {

    public Map<Users, BigDecimal> calculateSplitFromMap(Map<Users, BigDecimal> percentageMap, BigDecimal totalAmount) {
        Map<Users, BigDecimal> result = new HashMap<>();

        for (Map.Entry<Users, BigDecimal> entry : percentageMap.entrySet()) {
            BigDecimal share = totalAmount
                    .multiply(entry.getValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            result.put(entry.getKey(), share);
        }
        return result;
    }

    @Override
    public Map<Users, BigDecimal> calculateSplit(BigDecimal totalAmount, List<Users> users) {
        throw new UnsupportedOperationException("Percentage split requires external map input.");
    }
}
