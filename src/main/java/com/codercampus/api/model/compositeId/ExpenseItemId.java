package com.codercampus.api.model.compositeId;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ExpenseItemId implements Serializable {

    private Long expenseId;
    private Long itemId;

    public ExpenseItemId(Long expenseId, Long itemId) {
        expenseId = expenseId;
        itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ExpenseItemId that = (ExpenseItemId) o;
        return Objects.equals(expenseId, that.getExpenseId()) &&
                Objects.equals(itemId, that.getItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId, itemId);
    }
}
