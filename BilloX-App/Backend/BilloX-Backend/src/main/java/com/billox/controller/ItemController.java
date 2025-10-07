package com.billox.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.billox.io.ItemRequest;
import com.billox.io.ItemResponse;
import com.billox.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService iService;
	
	@PostMapping("/admin/items")
	@ResponseStatus(HttpStatus.CREATED)
	ItemResponse addItem(@RequestPart("item") String itemString, 
						 @RequestPart("file") MultipartFile file) throws IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		ItemRequest itemRequest = null;
		
		try {
			itemRequest = objectMapper.readValue(itemString, ItemRequest.class);
			
			return iService.add(itemRequest, file);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occured while parsing json: " + e.getMessage());
		}
		
	}
	
	@GetMapping("/items")
	List<ItemResponse> readItems(){
		
		return iService.fetchItems();
	}
	
	@DeleteMapping("/admin/items/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void removeItem(@PathVariable String itemId) {
		
		try {
			iService.deleteItems(itemId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
		}
	}
	
}
