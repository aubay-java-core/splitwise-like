
package pt.community.java.splitwise_like.expenses.splitstrategy;

import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("exactSplitStrategy")
public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public Map<Users, BigDecimal> calculateSplit(BigDecimal totalAmount, List<Users> users) {
        throw new UnsupportedOperationException("Exact split requires external map input.");
    }

}
