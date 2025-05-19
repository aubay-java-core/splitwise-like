
package pt.community.java.splitwise_like.expenses.splitstrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.community.java.splitwise_like.expenses.enums.SplitMethodEnum;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SplitStrategyFactory {

    private final Map<String, SplitStrategy> strategyMap;

    public SplitStrategy getStrategy(SplitMethodEnum method) {
        return switch (method) {
            case EQUAL -> strategyMap.get("equalSplitStrategy");
            case EXACT -> strategyMap.get("exactSplitStrategy");
            case PERCENTAGE -> strategyMap.get("percentageSplitStrategy");
            case WEIGHTED -> strategyMap.get("weightedSplitStrategy");
            case CUSTOM_EQUAL -> strategyMap.get("customEqualSplitStrategy");
            default -> throw new UnsupportedOperationException("Unsupported strategy: " + method);
        };
    }
}
