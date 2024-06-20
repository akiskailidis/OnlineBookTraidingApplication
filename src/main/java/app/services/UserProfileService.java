package app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import app.model.Book;
import app.model.Recommendations;
import app.model.Search;
import app.model.UserProfile;



@Service
public interface UserProfileService {
	
	public UserProfile retrieveProfile(String username);
	
	public void save(UserProfile userProfile);
	
	public List <Book> retrieveBookOffers(String username);
	
	public void addBookOffer(String username, Book book);
	
	public List <Book> searchBooks(Search search);
	
	public List <Book> recommendBooks(String username); 
	
	public void requestBook(int bookId,String username);
	
	public List<Book>retrieveBookRequests(String username);
	
	public List<UserProfile>retrieveRequestingUsers(int bookId);
	
	public void deleteBookOffers(String username, int bookId);
	
	public void deleteBookRequest(String username, int bookId);

	public Recommendations getRecommendations(String username);
	
	
	
	
	

}
