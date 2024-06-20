package app.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.User;

public interface UserMapper extends JpaRepository<User,Integer>{
	
	public Optional<User>findByUsername(String username);

}
