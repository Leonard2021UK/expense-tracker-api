package com.codercampus.api.repository.views;

import com.codercampus.api.model.views.ItemCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategorySumViewRepo extends JpaRepository<ItemCategoryView, Long> {
//    @Query(nativeQuery = true, value = "SELECT ic.name as category, ei.item_category_id as categoryId, sum(ei.price) as total,i.user_id  FROM expense_item ei\n" +
//            "    LEFT JOIN item i on i.id = ei.item_id\n" +
//            "    JOIN item_category ic on ei.item_category_id = ic.id\n" +
//            "    where i.user_id = :userId and i.created_at >= DATE_SUB(NOW(), INTERVAL :period DAY) group by ei.item_category_id ,i.user_id ")
    @Query(value = "SELECT itcsum100 FROM itemcatpricesum100 itcsum100 WHERE itcsum100.uid = :userId")
    List<ItemCategoryView> findAllItemCategoryByPeriod(Long userId);


}
