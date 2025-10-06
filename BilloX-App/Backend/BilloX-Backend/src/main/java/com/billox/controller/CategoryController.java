package com.billox.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.billox.io.CategoryRequest;
import com.billox.io.CategoryResponse;
import com.billox.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService cService;

	@PostMapping("/admin/categories")
	@ResponseStatus(HttpStatus.CREATED)
	CategoryResponse addCategory(@RequestPart("category") String categoryString, 
								 @RequestPart("file") MultipartFile file
								) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		CategoryRequest request = null;
		
		try {
			request = objectMapper.readValue(categoryString, CategoryRequest.class);
			
			
			return cService.add(request, file);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occured while parsing json: " + e.getMessage());
		}

	}
	
	@GetMapping
	List<CategoryResponse> fetchCategories(){
		
		return cService.read();
		
	}
	
	@DeleteMapping("/admin/categories/{categoryId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void remove(@PathVariable String categoryId) {
		
		try {
			cService.delete(categoryId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		
	}

}
