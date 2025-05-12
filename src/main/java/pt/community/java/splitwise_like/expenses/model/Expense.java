package pt.community.java.splitwise_like.expenses.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "expense_participants",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Users> participants = new HashSet<>();

}