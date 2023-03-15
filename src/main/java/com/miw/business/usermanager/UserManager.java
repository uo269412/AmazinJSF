
package com.miw.business.usermanager;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.model.User;
import com.miw.persistence.user.UserDataService;

@Named("userManagerService")
@ApplicationScoped
public class UserManager implements UserManagerService {
	Logger logger = LogManager.getLogger(this.getClass());

	@Inject
	private UserDataService userDataService = null;

	@Override
	public List<User> getUsers() throws Exception {
		logger.debug("Asking for users");
		return userDataService.getUsers();
	}

	@Override
	public void addUser(User user) throws Exception {
		logger.debug("Inserting users");
		userDataService.addUser(user);

	}

	@Override
	public Optional<User> getUser(String login) throws Exception {
		logger.debug("Getting user with login " + login);
		return userDataService.getUser(login);
	}
}
