package com.miw.persistence.book;

import java.util.List;
import java.util.Optional;

import com.miw.model.Book;

public interface BookDataService {

	List<Book> getBooks() throws Exception;
	
	void addBook(Book book) throws Exception;

	Optional<Book> getBook(int id) throws Exception;

	void updateBook(Book book) throws Exception;

	Optional<Book> getBookByTitle(String title);

}