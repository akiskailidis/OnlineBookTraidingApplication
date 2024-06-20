package app.services;

import org.springframework.stereotype.Service;

import app.model.User;
@Service
public interface UserService {

	public void saveUser(User user);
	
	public boolean isUserPresent(User user);
	
	public User findById(String Username);
	
}
