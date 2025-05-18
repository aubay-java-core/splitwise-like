package pt.community.java.splitwise_like.transactions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.community.java.splitwise_like.expenses.model.Expense;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private Users fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private Users toUser;

    @Column(nullable = false)
    private LocalDateTime createdAt;


}