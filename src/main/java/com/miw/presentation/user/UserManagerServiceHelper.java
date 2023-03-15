package com.miw.presentation.user;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.miw.business.usermanager.UserManagerService;
import com.miw.model.User;

@Named("userManagerServiceHelper")
@ApplicationScoped
public class UserManagerServiceHelper {

	Logger logger = LogManager.getLogger(this.getClass());
	@Inject
	private UserManagerService userManagerService = null;

	public UserManagerService getUserManagerService() {
		return userManagerService;
	}

	public void setUserManagerService(UserManagerService userManagerService) {
		logger.debug("Injecting userManagerService");
		this.userManagerService = userManagerService;
	}
	
	public boolean checkIfUserExists(User user) {
		logger.debug("Executing GetUserCommand");
		Optional<User> result = Optional.empty();
		try {
			result = userManagerService.getUser(user.getLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.isPresent();
	}
	
	public void insertIntoDatabase(User user) {
		logger.debug("Executing AddUserCommand");
		try {
			userManagerService.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Optional<User> getUserByLogin(String login) {
		logger.debug("Executing GetUserCommand");
		Optional<User> result = Optional.empty();
		try {
			result = userManagerService.getUser(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}
