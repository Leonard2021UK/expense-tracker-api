package com.codercampus.api.model.compositeId;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ExpenseItemId implements Serializable {

    private Long rowId;
    private Long expenseId;
    private Long itemId;

    public ExpenseItemId(Long expenseId, Long itemId, Long rowId) {
        this.expenseId = expenseId;
        this.itemId = itemId;
        this.rowId = rowId;
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
