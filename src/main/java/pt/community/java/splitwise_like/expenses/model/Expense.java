package pt.community.java.splitwise_like.expenses.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.community.java.splitwise_like.groups.model.Group;

import java.math.BigDecimal;

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

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
