package com.miw.model;

import java.util.HashMap;

public class ShoopingCart {

	private HashMap<String, Integer> list;
	private double totalPrice;

	public ShoopingCart() {
		this.list = new HashMap<String, Integer>();
		this.totalPrice = 0;
	}

	public HashMap<String, Integer> getList() {
		return list;
	}

	public void add(String book) {
		if (list.containsKey(book))
			list.replace(book, list.get(book) + 1);
		else
			list.put(book, 1);
	}

	public void addPrice(double price) {
		this.totalPrice += price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void reset() {
		this.list = new HashMap<String, Integer>();
		this.totalPrice = 0;
	}
}
