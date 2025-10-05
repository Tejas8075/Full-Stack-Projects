package com.billox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.billox.io.CategoryRequest;
import com.billox.io.CategoryResponse;
import com.billox.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

	private final CategoryService cService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	CategoryResponse addCategory(@RequestBody CategoryRequest request) {

		return cService.add(request);

	}
	
	@GetMapping
	List<CategoryResponse> fetchCategories(){
		
		return cService.read();
		
	}
	
	@DeleteMapping("/{categoryId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void remove(@PathVariable String categoryId) {
		
		try {
			cService.delete(categoryId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
	}

}
