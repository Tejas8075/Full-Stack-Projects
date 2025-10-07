package com.billox.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.billox.io.ItemRequest;
import com.billox.io.ItemResponse;

public interface ItemService {

	ItemResponse add(ItemRequest request, MultipartFile file) throws IOException;
	
	List<ItemResponse> fetchItems();
	
	void deleteItems(String itemId);
	
}
