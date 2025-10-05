package com.billox.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.billox.config.StaticResourceConfig;
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

	private final StaticResourceConfig sRConfig;

	@Override
	public CategoryResponse add(CategoryRequest request, MultipartFile file) throws IOException {

		String fileName = UUID.randomUUID().toString() + "."
				+ StringUtils.getFilenameExtension(file.getOriginalFilename());

//		Get the uploads directory
		Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

		Files.createDirectories(uploadPath);

		Path targetLocation = uploadPath.resolve(fileName);

		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		String imagUrl = "http://localhost:8080/api/v1.0/uploads/" + fileName;

		CategoryEntity newCategory = convertToEntity(request);

		newCategory.setImgUrl(imagUrl);

		newCategory = cRepo.save(newCategory);

		return convertToResponse(newCategory);

	}

	@Override
	public List<CategoryResponse> read() {

		return cRepo.findAll().stream().map(categoryEntity -> convertToResponse(categoryEntity))
				.collect(Collectors.toList());

	}

	@Override
	public void delete(String categoryId) {

		CategoryEntity existingCategory = cRepo.findByCategoryId(categoryId)
				.orElseThrow(() -> new RuntimeException("Category Not Found: " + categoryId));
		
		String imgUrl = existingCategory.getImgUrl();
		
		String fileName =  imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
		
		Path uploadPath =  Paths.get("uploads").toAbsolutePath().normalize();
		
		Path filePath = uploadPath.resolve(fileName);
		
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		cRepo.delete(existingCategory);

	}

	private CategoryResponse convertToResponse(CategoryEntity newCategory) {

		return CategoryResponse.builder().categoryId(newCategory.getCategoryId()).name(newCategory.getName())
				.description(newCategory.getDescription()).bgColor(newCategory.getBgColor())
				.imgUrl(newCategory.getImgUrl()).createdAt(newCategory.getCreatedAt())
				.updatedAt(newCategory.getUpdatedAt()).build();

	}

	private CategoryEntity convertToEntity(CategoryRequest request) {

		return CategoryEntity.builder().categoryId(UUID.randomUUID().toString()).name(request.getName())
				.description(request.getDescription()).bgColor(request.getBgColor()).build();

	}

}
