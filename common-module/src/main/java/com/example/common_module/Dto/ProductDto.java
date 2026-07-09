package com.example.common_module.Dto;

public class ProductDto {
	private Long id;
	private String name;
	private double price;
	 public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	 private String imageUrl;
	public ProductDto() {
		
	}
	
	public ProductDto(Long id, String name, double price, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl=imageUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}	
	
}
