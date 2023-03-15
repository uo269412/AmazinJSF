package com.miw.persistence.user;

import java.util.List;
import java.util.Optional;

import com.miw.model.User;

public interface UserDataService {

	List<User> getUsers() throws Exception;
	void addUser(User user) throws Exception;
	Optional<User> getUser(String login) throws Exception;

}