package pt.community.java.splitwise_like.transactions.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse implements Serializable {

    private String receiver;
    private BigDecimal amount;
    private String Description;
    private LocalDateTime date;
}
