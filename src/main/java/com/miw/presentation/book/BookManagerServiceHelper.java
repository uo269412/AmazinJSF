package com.miw.presentation.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.business.bookmanager.BookManagerService;
import com.miw.model.Book;
import com.miw.presentation.CounterBean;

@Named("bookManagerServiceHelper")
@ApplicationScoped
public class BookManagerServiceHelper {

	Logger logger = LogManager.getLogger(this.getClass());

	@Inject
	private CounterBean counter = null;

	@Inject
	private BookManagerService bookManagerService = null;

	public void setBookManagerService(BookManagerService bookManagerService) {
		logger.debug("Injecting bookManagerService");
		this.bookManagerService = bookManagerService;
	}

	public CounterBean getCounter() {
		return counter;
	}

	public void setCounter(CounterBean counter) {
		logger.debug("Injecting the counter");
		this.counter = counter;
	}

	public List<Book> getBooks() throws Exception {
		logger.debug("Retrieving Books from Business Layer and incrementing the counter");
		counter.inc();
		return bookManagerService.getBooks();
	}

	public Book getSpecialOffer() throws Exception {
		logger.debug("Retrieving Special Offer from Business Layer");
		return bookManagerService.getSpecialOffer();
	}

	public List<Book> booksUserCanAddToCart(HashMap<String, Integer> shoppingCart) {
		logger.debug("Redirecting to showBooks view with success");
		List<Book> booksAvailableToAdd = new ArrayList<Book>();
		try {
			List<Book> books = getBooks();
			for (Book book : books) {
				int quantityInShoppingCart = 0;
				if (shoppingCart.get(book.getTitle()) != null) {
					quantityInShoppingCart = shoppingCart.get(book.getTitle());
				}
				book.setStock(book.getStock() - quantityInShoppingCart);
				bookManagerService.calculatePrice(book);
				if (book.getStock() > 0) {
					booksAvailableToAdd.add(book);
				}
			}
		} catch (Exception e) {
			System.out.println("horror");
			e.printStackTrace();
		}
		return booksAvailableToAdd;
	}

	public boolean updateBook(Book book) {
		Optional<Book> opBook = Optional.empty();
		try {
			opBook = bookManagerService.getBookByTitle(book.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (opBook.isEmpty()) {
			return false;
		} else {
			Book b = opBook.get();
			b.setStock(book.getStock());
			try {
				bookManagerService.updateBook(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	public boolean insertBook(Book book) {
		Optional<Book> opBook = Optional.empty();
		try {
			opBook = bookManagerService.getBookByTitle(book.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (opBook.isEmpty()) {
			try {
				book.setStock(10);
				bookManagerService.addBook(book);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public Book getBookByTitleAndUpdatePriceAccordingToStock(String title) {
		try {
			Book book = bookManagerService.getBookByTitle(title).get();
			bookManagerService.calculatePrice(book);
			return book;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean canPurchaseBook(String title, int quantity) {
		try {
			Book book = bookManagerService.getBookByTitle(title).get();
			if (book.getStock() - quantity >= 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateBookStock(String key, Integer value) {
		Book book = new Book();
		try {
			book = bookManagerService.getBookByTitle(key).get();
			book.setStock(book.getStock() - value);
			bookManagerService.updateBook(book);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
