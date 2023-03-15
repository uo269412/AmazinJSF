package com.miw.presentation.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.model.Book;
import com.miw.model.ShoopingCart;
import com.miw.model.User;
import com.miw.model.UserBean;
import com.miw.presentation.book.BookManagerServiceHelper;
import com.miw.presentation.user.UserManagerServiceHelper;

@Named
@SessionScoped
public class Controller implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4152944233217224080L;

	Logger logger = LogManager.getLogger(this.getClass());

	@Inject
	private BookManagerServiceHelper bookManagerServiceHelper = null;

	@Inject
	private UserManagerServiceHelper userManagerServiceHelper = null;

	public BookManagerServiceHelper getBookManagerServiceHelper() {
		return bookManagerServiceHelper;
	}

	public void setBookManagerServiceHelper(BookManagerServiceHelper bookManagerServiceHelper) {
		this.bookManagerServiceHelper = bookManagerServiceHelper;
	}

	public UserManagerServiceHelper getUserManagerServiceHelper() {
		return userManagerServiceHelper;
	}

	public void setUserManagerServiceHelper(UserManagerServiceHelper userManagerServiceHelper) {
		this.userManagerServiceHelper = userManagerServiceHelper;
	}

	private UserBean loginInfo = new UserBean();
	private ShoopingCart shoopingCart = new ShoopingCart();
	private Map<String, Boolean> checkMap = new HashMap<String, Boolean>();
	private User user = new User();
	private Book book = new Book();
	private List<Book> booksAvailableToAdd = new ArrayList<Book>();

	public String login() {
		logger.debug("doing login with " + loginInfo);
		FacesMessage msg;
		Optional<User> userOp = userManagerServiceHelper.getUserByLogin(loginInfo.getLogin());
		if (userOp.isEmpty()) {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error: User does not exist in the database",
					"User does not exist in the database");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return "login-error";
		} else {
			User userPre = userOp.get();
			if (userPre.getLogin().equals(loginInfo.getLogin())
					&& userPre.getPassword().equals(loginInfo.getPassword())) {
				setUser(userPre);
				System.out.println(user.isAdmin());
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@ " + loginInfo.getLogin(),
						loginInfo.getLogin());
				FacesContext.getCurrentInstance().addMessage("welcome", msg);
				return "success";
			}
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error: Username and password are incorrect",
					"Username and password does not match");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "login-error";
		}
	}

	public String register() {
		logger.debug("doing register " + user);
		FacesMessage msg;
		if (userManagerServiceHelper.checkIfUserExists(user)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error: User already exists",
					"User already exists");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "register-error";
		}
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@ " + user.getLogin(), loginInfo.getLogin());
		FacesContext.getCurrentInstance().addMessage("welcome", msg);
		userManagerServiceHelper.insertIntoDatabase(user);
		return "success";
	}

	public String logout() {
		FacesMessage msg;
		setLoginInfo(new UserBean());
		setUser(new User());
		setShoopingCart(new ShoopingCart());
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout: User correctly logged out",
				"Logout: User correctly logged out");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		return "success";
	}

	public String showBooksAction() {
		logger.debug("Redirecting to showBooks view with success");
		try {
			booksAvailableToAdd = bookManagerServiceHelper.getBooks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public String showSpecialOfferAction() {
		logger.debug("Redirecting to showSpecialOffer view");
		return "success";
	}

	public String startAction() {
		return "success";
	}

	public String showCart() {
		updateShoppingCart();
		return "success";
	}

	public void updateShoppingCart() {
		for (Entry<String, Integer> entry : shoopingCart.getList().entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				Book b = bookManagerServiceHelper.getBookByTitleAndUpdatePriceAccordingToStock(entry.getKey());
				if (b != null) {
					shoopingCart.addPrice(b.getPrice());
				}
			}
		}
	}

	public String showLoginForm() {
		return "success";
	}

	public String showRegisterForm() {
		return "success";
	}

	public String showInsertBookPage() {
		setBook(new Book());
		return "success";
	}

	public String showUpdateBookStockPage() {
		setBook(new Book());
		return "success";
	}

	public String insertBook() {
		FacesMessage msg;
		if (!bookManagerServiceHelper.insertBook(book)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add book error: Book with same title already exists",
					"Book with same title already exists");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "insert-error";
		}
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Add book: Book correctly added", "Book correctly added");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "success";
	}

	public String updateStock() {
		FacesMessage msg;
		if (!bookManagerServiceHelper.updateBook(book)) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update book error: Book with that title doesn't exist",
					"Book with that title doesn't exist");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "update-error";
		}
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Update book: Book correctly updated",
				"Book correctly updated");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "success";
	}

	public String addBooks() {
		booksAvailableToAdd = bookManagerServiceHelper.booksUserCanAddToCart(shoopingCart.getList());
		checkMap.clear();
		for (Book book : booksAvailableToAdd)
			checkMap.put(book.getTitle(), Boolean.FALSE);
		return "success";

	}

	public String addProcess() {
		for (String title : checkMap.keySet())
			if (checkMap.get(title))
				shoopingCart.add(title);
		;
		updateShoppingCart();
		return "success";
	}

	public String purchase() {
		FacesMessage msg;
		for (Entry<String, Integer> entry : shoopingCart.getList().entrySet()) {
			if (!bookManagerServiceHelper.canPurchaseBook(entry.getKey(), entry.getValue())) {
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Purchase books error: No longer have enough stock for this purchase",
						"No longer have enough stock for this purchase");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return "purchase-error";
			}
		}
		for (Entry<String, Integer> entry : shoopingCart.getList().entrySet()) {
			bookManagerServiceHelper.updateBookStock(entry.getKey(), entry.getValue());
		}
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Purchase books: Books correctly purchased",
				"Books correctly purchased");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		shoopingCart.reset();
		return "success";
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Map<String, Boolean> getCheckMap() {
		return checkMap;
	}

	public void setCheckMap(Map<String, Boolean> checkMap) {
		this.checkMap = checkMap;
	}

	public ShoopingCart getShoopingCart() {
		return shoopingCart;
	}

	public void setShoopingCart(ShoopingCart shoopingCart) {
		this.shoopingCart = shoopingCart;
	}

	public UserBean getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(UserBean loginInfo) {
		logger.debug("Setting loginInfo");
		this.loginInfo = loginInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		logger.debug("Setting user");
		this.user = user;
	}

	public List<Book> getBooksAvailableToAdd() {
		return booksAvailableToAdd;
	}

	public void setBooksAvailableToAdd(List<Book> booksAvailableToAdd) {
		this.booksAvailableToAdd = booksAvailableToAdd;
	}

}
