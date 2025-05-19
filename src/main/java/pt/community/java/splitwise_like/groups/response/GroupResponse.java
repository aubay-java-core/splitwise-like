package pt.community.java.splitwise_like.groups.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pt.community.java.splitwise_like.expenses.response.ExpenseResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GroupResponse {
    private Long groupId;
    private String groupName;
    private List<String> participants; // Lista de nomes ou e-mails dos participantes
    private List<ExpenseResponse> expenses; // Lista de despesas simplificada
}