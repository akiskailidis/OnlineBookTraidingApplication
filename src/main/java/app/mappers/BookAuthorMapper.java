package app.mappers;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import app.model.BookAuthor;


public interface BookAuthorMapper extends JpaRepository<BookAuthor,String> {
	
	public List<BookAuthor>findByName(String name);
	public BookAuthor findOneByName(String name);
  
}
