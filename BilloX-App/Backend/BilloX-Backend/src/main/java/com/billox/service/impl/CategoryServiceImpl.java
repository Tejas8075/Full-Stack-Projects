package com.billox.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.billox.entity.CategoryEntity;
import com.billox.io.CategoryRequest;
import com.billox.io.CategoryResponse;
import com.billox.repository.CategoryRepository;
import com.billox.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository cRepo;
	
	@Override
	public CategoryResponse add(CategoryRequest request) {

		CategoryEntity newCategory = convertToEntity(request);
		
		newCategory = cRepo.save(newCategory);
		
		return convertToResponse(newCategory);

	}

	private CategoryResponse convertToResponse(CategoryEntity newCategory) {
		
		return CategoryResponse.builder()
							   .categoryId(newCategory.getCategoryId())
							   .name(newCategory.getName())
							   .description(newCategory.getDescription())
							   .bgColor(newCategory.getBgColor())
							   .imgUrl(newCategory.getImgUrl())
							   .createdAt(newCategory.getCreatedAt())
							   .updatedAt(newCategory.getUpdatedAt())
							   .build();
		
	}

	private CategoryEntity convertToEntity(CategoryRequest request) {
		
		return CategoryEntity.builder()
					  .categoryId(UUID.randomUUID().toString())
					  .name(request.getName())
					  .description(request.getDescription())
					  .bgColor(request.getBgColor())
					  .build();
		
	}

	@Override
	public List<CategoryResponse> read() {

		return cRepo.findAll()
			 .stream()
			 .map(categoryEntity -> convertToResponse(categoryEntity))
			 .collect(Collectors.toList());
		
	}

	@Override
	public void delete(String categoryId) {
		
		CategoryEntity existingCategory = cRepo.findByCategoryId(categoryId)
			 .orElseThrow(() -> new RuntimeException("Category Not Found: " + categoryId));
		
		cRepo.delete(existingCategory);
		
	}

}
