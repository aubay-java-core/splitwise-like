package pt.community.java.splitwise_like.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "split_detail")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SplitDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long splitDetailId;
    private double amount;
    private double percentage; // Para PERCENT_SPLIT

    @ManyToOne
    private Users user;

    @ManyToOne
    private Transaction transaction;
}
