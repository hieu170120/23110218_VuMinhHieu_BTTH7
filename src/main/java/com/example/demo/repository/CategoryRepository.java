package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.entity.CategoriesEntity;

public interface CategoryRepository extends JpaRepository<CategoriesEntity, Long> {
	List<CategoriesEntity> findByNameContaining(String name);
	Page<CategoriesEntity> findByNameContaining(String name, Pageable pageable);

}
