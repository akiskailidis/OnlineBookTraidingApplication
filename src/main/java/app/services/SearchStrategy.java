package app.services;
import java.util.List;

import app.mappers.BookMapper;
import app.model.Book;
import app.model.Search;

public interface SearchStrategy {
	
	public List<Book> search(Search search ,BookMapper bookMapper);
}
