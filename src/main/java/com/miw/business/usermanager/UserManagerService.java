package com.miw.business.usermanager;

import java.util.List;
import java.util.Optional;

import com.miw.model.User;

public interface UserManagerService {
	public List<User> getUsers() throws Exception;
	public void addUser(User user) throws Exception;
	public Optional<User> getUser(String login) throws Exception;

}