package com.billox.io;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {

	private String categoryId;

	private String name;

	private String description;

	private String bgColor;

	private String imgUrl;

	private Timestamp createdAt;

	private Timestamp updatedAt;

}
