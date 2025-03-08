package pt.community.java.splitwise_like.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "expense")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    private double amount;
    private String description;
    private SplitMethod splitMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "expense",fetch = FetchType.LAZY)
    private List<Transaction> transactions;

}
