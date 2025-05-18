package pt.community.java.splitwise_like.expenses.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.community.java.splitwise_like.users.model.Users;

import java.math.BigDecimal;

@Entity
@Table(name = "split_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SplitDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Expense expense;

    @ManyToOne(optional = false)
    private Users user;

    @Column(nullable = false)
    private BigDecimal amount;
}