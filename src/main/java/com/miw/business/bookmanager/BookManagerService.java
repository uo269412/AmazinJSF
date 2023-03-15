package com.miw.business.bookmanager;

import java.util.List;
import java.util.Optional;

import com.miw.model.Book;

public interface BookManagerService {
	public List<Book> getBooks() throws Exception;
	public Book getSpecialOffer() throws Exception;
	public void addBook(Book book) throws Exception;
	public Optional<Book> getBook(int i) throws Exception;
	public Optional<Book> getBookByTitle(String title) throws Exception;
	public void updateBook(Book book) throws Exception;
	public void calculatePrice(Book book);
}