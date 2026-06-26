package com.trier.trier_report.dao;

import com.trier.trier_report.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
