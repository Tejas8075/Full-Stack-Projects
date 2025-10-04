package com.billox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billox.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	Optional<CategoryEntity> findByCategoryId(String categoryId);
	
}
