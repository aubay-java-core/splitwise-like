package pt.community.java.splitwise_like.expenses.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.community.java.splitwise_like.expenses.enums.SplitMethodEnum;
import pt.community.java.splitwise_like.groups.model.Group;
import pt.community.java.splitwise_like.transactions.model.Transaction;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "expense")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitMethodEnum splitMethod;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paid_by_user_id")
    private Users paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ElementCollection
    @CollectionTable(name = "expense_splits", joinColumns = @JoinColumn(name = "expense_id"))
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "amount")
    private Map<Users, BigDecimal> splits = new HashMap<>();

    @Transient
    private Map<Users, BigDecimal> exactSplit;

    @Transient
    private Map<Users, BigDecimal> percentageSplit;


    @Transient
    private Map<Long, Integer> weightSplit;



}
