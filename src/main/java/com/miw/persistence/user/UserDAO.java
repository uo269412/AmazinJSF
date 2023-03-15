package com.miw.persistence.user;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.model.User;
import com.miw.persistence.Dba;

public class UserDAO implements UserDataService {

	protected Logger logger = LogManager.getLogger(getClass());

	public List<User> getUsers() throws Exception {

		List<User> resultList = null;

		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			resultList = em.createQuery("Select a From User a", User.class).getResultList();

			logger.debug("Result list: " + resultList.toString());
			for (User next : resultList) {
				logger.debug("next user: " + next);
			}

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}

		// We return the result
		return resultList;
	}

	@Override
	public void addUser(User user) throws Exception {
		Dba dba = new Dba();
		try {
			EntityManager em = dba.getActiveEm();

			em.persist(user);

			logger.debug("Inserted users " + user.toString());

		} finally {
			// 100% sure that the transaction and entity manager will be closed
			dba.closeEm();
		}
	}

	@Override
	public Optional<User> getUser(String login) throws Exception {
		Dba dba = new Dba();
		Optional<User> result;
		try {
			EntityManager em = dba.getActiveEm();

			TypedQuery<User> userquery = em.createQuery("Select a From User a where login = :loginquery", User.class);
			userquery.setParameter("loginquery", login);
			try {
				result = Optional.of(userquery.getSingleResult());

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
