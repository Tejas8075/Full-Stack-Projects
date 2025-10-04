package com.billox.service;

import java.util.List;

import com.billox.io.CategoryRequest;
import com.billox.io.CategoryResponse;

public interface CategoryService {

	CategoryResponse add(CategoryRequest request);
	
	List<CategoryResponse> read();
	
	void delete(String categoryId);
	
}
