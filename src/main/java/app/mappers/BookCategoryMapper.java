package app.mappers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import app.model.BookCategory;

public interface BookCategoryMapper extends JpaRepository<BookCategory,Integer>{
	
	public List<BookCategory> findByName(String name);
	
}
