package com.billox.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ITEMS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(unique = true)
	private String itemId;
	
	private String name;
	
	private BigDecimal price;
	
	private String description;
	
	private String imgUrl;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	// We cannot delete the category because the category will be mapped to Items
	@OnDelete(action = OnDeleteAction.RESTRICT)
	private CategoryEntity category;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdAt;
	
	@UpdateTimestamp
	@Column(insertable = false)
	private Timestamp updatedAt;
	
}
