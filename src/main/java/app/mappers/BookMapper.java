package app.mappers;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Book;
import app.model.BookAuthor;
import app.model.BookCategory;

public interface BookMapper extends JpaRepository<Book,Integer>{
	
	public List<Book> findByTitle(String title);
	public List<Book> findByTitleContaining(String title);
	public Book findByBookId(int bookid);
	List<Book> findByAuthorIn(List<BookAuthor> authors);
    List<Book> findByBookCategoryIn(List<BookCategory> categories);
    List<Book> findByAuthor(BookAuthor author);
	
}
