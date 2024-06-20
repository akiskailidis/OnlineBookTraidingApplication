package app.mappers;


import org.springframework.data.jpa.repository.JpaRepository;

import app.model.UserProfile;

public interface UserProfileMapper extends JpaRepository<UserProfile,Integer>{
	
	public UserProfile findByUsername(String username);

}
