package com.example.comp;

import android.graphics.Bitmap;

public class Product {
	

//	public enum Categories{BOOK, BEDDING, SPORT, FASHION, COOKING, ELECTRONICS}

	
	/**
	 * The state of the product
	 */
	private String quality;
	
	/**
	 * A short description of the project
	 */
	private String title;
	
	/**
	 * A long description of the product
	 */
	private String description;
	
	private Bitmap image;

	private int icon;

	
	// TODO: image of product

	/**
	 * The default constructor for the object product
	 * @param quality
	 * @param summary
	 * @param description
	 */
	public Product(String quality, String title, String description, int icon) {
		//super();
		this.quality = quality;
		this.title = title;
		this.description = description;
		this.icon = icon;
		
	}

	/**
	 * @return the summary
	 */
	public String gettitle() {
		return title;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the quality
	 */
	public String getQuality() {
		return quality;
	}
	
	public void setImage(Bitmap image){
		this.image = image;
	}
	
	public Bitmap getImage(){
		return image;
	}
	
	public int getIcon(){
		return icon;
	}
	
}