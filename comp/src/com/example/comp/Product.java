package com.example.comp;


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
	
	//private Bitmap image;



	
	// TODO: image of product

	/**
	 * The default constructor for the object product
	 * @param quality
	 * @param summary
	 * @param description
	 */
	public Product(String quality, String title, String description) {
		//super();
		this.quality = quality;
		this.title = title;
		this.description = description;
		
	}

	/**
	 * @return the summary
	 */
	public String getTitle() {
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
	
	public void setQuality(String quality){
		this.quality = quality;
	}
	
	/*public void setImage(Bitmap image){
		this.image = image;
	}
	
	public Bitmap getImage(){
		return image;
	}*/
	
}