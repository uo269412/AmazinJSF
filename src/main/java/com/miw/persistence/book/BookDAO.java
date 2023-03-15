package com.miw.persistence.book;

import java.util.List;
import java.util.Optional;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.*;

import com.miw.model.Book;
import com.miw.persistence.Dba;

@Named("bookDataService")
@RequestScoped
public class BookDAO implements BookDataService {

	protected Logger logger = LogManager.getLogger(getClass());

	public List<Book> getBooks() throws Exception {

		List<Book> resultList = null;

		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			resultList = em.createQuery("Select a From Book a", Book.class).getResultList();

			logger.debug("Result list: " + resultList.toString());
			for (Book next : resultList) {
				logger.debug("next book: " + next);
			}

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}

		// We return the result
		return resultList;
	}

	@Override
	public void addBook(Book book) throws Exception {
		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			em.merge(book);

			logger.debug("Inserted book " + book.toString());

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}
	}

	@Override
	public Optional<Book> getBook(int id) throws Exception {
		Dba dba = new Dba();
		Optional<Book> result;
		try {
			EntityManager em = dba.getActiveEm();

			TypedQuery<Book> bookquery = em.createQuery("Select a From Book a where id = :id", Book.class);
			bookquery.setParameter("id", id);
			try {
				result = Optional.of(bookquery.getSingleResult());

			} catch (NoResultException ex) {
				result = Optional.empty();
			}

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}
		return result;
	}

	@Override
	public void updateBook(Book book) throws Exception {
		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			em.merge(book);

			logger.debug("Updated book " + book.toString());

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}

	}

	@Override
	public Optional<Book> getBookByTitle(String title) {
		Dba dba = new Dba();
		Optional<Book> result;
		try {
			EntityManager em = dba.getActiveEm();

			TypedQuery<Book> bookquery = em.createQuery("Select a From Book a where title = :title", Book.class);
			bookquery.setParameter("title", title);
			try {
				result = Optional.of(bookquery.getSingleResult());

			} catch (NoResultException ex) {
				result = Optional.empty();
			}

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}
		return result;
	}

}