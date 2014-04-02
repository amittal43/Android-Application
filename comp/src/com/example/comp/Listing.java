package com.example.comp;

import java.util.Date;

public class Listing {

	/**
	 * The price of the product
	 */
	private int price;
	
	/**
	 * The date when the listing was submitted
	 */
	private Date date;
	
	/**
	 * The product concerned by the listing
	 */
	private Product product;

	/**
	 * The default constructor for the object listing.
	 * @param price
	 * @param product
	 */
	public Listing(int price, Product product) {
		this.price = price;
		this.date = new Date();
		this.product = product;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}
	
}