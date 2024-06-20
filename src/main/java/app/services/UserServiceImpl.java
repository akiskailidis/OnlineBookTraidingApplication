package app.services;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.mappers.UserMapper;
import app.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{
	
	
	@Autowired
	private BCryptPasswordEncoder bCpyptPasswordEncoder; 
	
	@Autowired
	private UserMapper usermapper; 

	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<User> storedUser = usermapper.findByUsername(username);
		User tempUser = storedUser.get(); 					// retrieving the user values out of the optional<User>
		return tempUser;
	}

	@Override
	public void saveUser(User user) {
		String encodedPassword = bCpyptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        usermapper.save(user);		
	}

	@Override
	public boolean isUserPresent(User user) {
		Optional<User> storedUser = usermapper.findByUsername(user.getUsername());
		return storedUser.isPresent();
	}

	@Override
	public User findById(String Username) {
		Optional<User> storedUser = usermapper.findByUsername(Username);
		User tempUser = storedUser.get(); 					// retrieving the user values out of the optional<User>
		return tempUser;
	}

}
