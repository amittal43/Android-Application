package com.example.comp;

import java.util.ArrayList;
import java.util.List;
import android.widget.ImageButton;

public class Product {
	

	public enum Categories{BOOK, BEDDING, SPORT, FASHION, COOKING, ELECTRONICS}
	
	/**
	 * The state of the product
	 */
	private int condition;
	
	/**
	 * A short description of the project
	 */
	private String summary;
	
	/**
	 * A long description of the product
	 */
	private String description;
	
	//private ImageButton image;

	
	// TODO: image of product

	/**
	 * The default constructor for the object product
	 * @param quality
	 * @param summary
	 * @param description
	 */
	public Product(int condition, String summary, String description) {
		//super();
		this.condition = condition;
		this.summary = summary;
		this.description = description;
		
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
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
	public int getCondition() {
		return condition;
	}
	
}