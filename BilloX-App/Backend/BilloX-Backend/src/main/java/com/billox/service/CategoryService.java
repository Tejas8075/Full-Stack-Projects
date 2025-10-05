package com.billox.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.billox.io.CategoryRequest;
import com.billox.io.CategoryResponse;

public interface CategoryService {

	CategoryResponse add(CategoryRequest request, MultipartFile file) throws IOException;
	
	List<CategoryResponse> read();
	
	void delete(String categoryId);
	
}
