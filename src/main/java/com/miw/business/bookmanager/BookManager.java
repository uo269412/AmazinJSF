
package com.miw.business.bookmanager;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.model.Book;
import com.miw.persistence.book.BookDataService;
//import com.miw.persistence.vat.VATDataService;
import com.miw.persistence.vat.VATDataService;

@Named ("bookManagerService")
@ApplicationScoped
public class BookManager implements BookManagerService {
	Logger logger = LogManager.getLogger(this.getClass());
	
	@Inject
	private BookDataService bookDataService = null;
	
	@Inject
	private VATDataService vatDataService = null;
	
	public void setVatDataService(VATDataService vatDataService) {
		logger.debug("Injecting vatDataService");
		this.vatDataService = vatDataService;
	}

	public void setBookDataService(BookDataService bookDataService) {
		logger.debug("Injecting bookDataService");
		this.bookDataService = bookDataService;
	}


	public List<Book> getBooks() throws Exception {
		logger.debug("Asking for books");
		List<Book> books = bookDataService.getBooks();
		
		// We calculate the final price with the VAT value
		for (Book b : books) {
			calculatePrice(b);
		}
		return books;
	}
	
	public Book getSpecialOffer() throws Exception
	{
		List<Book> books = getBooks();
		int number = (int) (Math.random()*books.size());
		logger.debug("Applying disccount to "+books.get(number).getTitle());
		books.get(number).setPrice((double)books.get(number).getPrice()*0.85);
		return books.get(number);
	}
	
	@Override
	public void addBook(Book book) throws Exception {
		logger.debug("Inserting book");
		book.setVat(vatDataService.getVAT(book.getTaxReference()));
		bookDataService.addBook(book);
	}

	public Optional<Book> getBook(int id) throws Exception {
		logger.debug("Getting book with id " + id);
		return bookDataService.getBook(id);
	}
	

	public void updateBook(Book book) throws Exception {
		logger.debug("Updating book  " + book.toString());
		calculatePrice(book);
		bookDataService.updateBook(book);
	}

	public Optional<Book> getBookByTitle(String title) throws Exception {
		logger.debug("Getting book with title " + title);
		return bookDataService.getBookByTitle(title);
	}
	
	
	public void calculatePrice(Book book) {
		book.setPrice(book.getBasePrice() * (1 + book.getVat().getValue()));
		if (book.getStock() == 10) {
			book.setPrice(book.getPrice()- (book.getPrice() * 0.05));
			book.setMessage("¡Oferta!");
		} else if (book.getStock() < 3 && book.getStock() > 0) {
			book.setPrice(book.getPrice() + (book.getPrice() * 0.05));
			book.setMessage("¡Últimas unidades!");
		} else {
			book.setPrice(book.getPrice());
			book.setMessage("");
		}
		book.setPrice(Math.round(book.getPrice() * 100.0) / 100.0);
	}
}
