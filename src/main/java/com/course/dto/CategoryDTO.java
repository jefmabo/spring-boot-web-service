package com.course.dto;

import java.io.Serializable;

import com.course.entities.Category;

public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public CategoryDTO(String name) {
		this.name = name;
	}

	public CategoryDTO(Long id, String name) {
		this.name = name;
		this.id = id;
	}
	
	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category toEntity() {
		return new Category(id, name);
	}
}
