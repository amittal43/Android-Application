package com.example.comp;

import java.util.ArrayList;
import java.util.Date;


public class Listing {

	public static ArrayList<Listing> bookListing = new ArrayList<Listing>();;
	public static ArrayList<Listing> beddingListing = new ArrayList<Listing>();;
	public static ArrayList<Listing> sportListing = new ArrayList<Listing>();;
	public static ArrayList<Listing> fashionListing = new ArrayList<Listing>();;
	public static ArrayList<Listing> cookingListing = new ArrayList<Listing>();;
	public static ArrayList<Listing> electronicsListing = new ArrayList<Listing>();
	//ArrayList<Listing> BooksListing;
	
//	public static ArrayList<Listing> collection = new ArrayList<Listing>();
	
	
	/*public ArrayList<Listing> createListing(){
		BeddingListing.add(new Listing(10, new Product("Good", "blanket" , " ", R.drawable.blanket)));
		return BeddingListing;
	}*/
	

	
	/**
	 * The price of the product
	 */
	private double price;
	
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
	public Listing(double price, Product product) {
		this.price = price;
		this.date = new Date();
		this.product = product;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
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
	
	/*public ArrayList<Listing> getBeddingListing(){
		return BeddingListing;
	}*/
	
}