package app.services;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import app.model.Book;
import app.model.BookAuthor;
import app.model.UserProfile;

@Service
public class AuthorRecommendationStrategy implements RecommendationsStrategy {

    @Override
    public List<Book> recommendBooks(UserProfile userProfile) {
    	List<Book> recommendedBookList = new ArrayList<Book>();
        for(BookAuthor bookAuthor:userProfile.getFavouriteBookAuthors()) {
        	if(!bookAuthor.getBooks().isEmpty()) {
	        	for(Book book:bookAuthor.getBooks()){
	        		recommendedBookList.add(book);
	        	}
        	}else {
        		throw new IllegalArgumentException("Book author has no books");
        	}
        }
        return recommendedBookList;
    }
}
