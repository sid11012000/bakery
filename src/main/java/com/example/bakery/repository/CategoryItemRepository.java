package com.example.bakery.repository;

import com.example.bakery.entity.CategoryItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
@Query("""
        SELECT c FROM CategoryItem c
        WHERE lower(c.category) LIKE lower(concat('%', :keyword, '%'))
           OR lower(c.subCategory) LIKE lower(concat('%', :keyword, '%'))
           OR lower(c.itemName) LIKE lower(concat('%', :keyword, '%'))
    """)
    List<CategoryItem> search(@Param("keyword") String keyword);
}
