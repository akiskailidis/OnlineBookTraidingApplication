package app.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="user_profiles")
public class UserProfile {

	@Id
	@Column(name="user_name", unique=true)
    private String username;

	@Column(name="fullname")
    private String fullName;

	@Column(name="age")
    private int age;
	
	@Column(name="phoneNumber")
    private Long phoneNumber;
	
	@Column(name="address")
    private String address;

    @ManyToMany
    private List<BookAuthor> favouriteBookAuthors;

    @ManyToMany
    private List<BookCategory> favouriteBookCategories;

    @ManyToMany
    private List<Book> bookOffers;

    @ManyToMany
    private List<Book> bookRequests;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    public UserProfile() {
		// TODO Auto-generated constructor stub
	}
    
    public UserProfile(String username, String fullName, int age, List<BookAuthor> favouriteBookAuthors,
			List<BookCategory> favouriteBookCategories, List<Book> bookOffers, List<Book> bookRequests,Long phoneNumber , String address) {
		this.username = username;
		this.fullName = fullName;
		this.age = age;
		this.favouriteBookAuthors = favouriteBookAuthors;
		this.favouriteBookCategories = favouriteBookCategories;
		this.bookOffers = bookOffers;
		this.bookRequests = bookRequests;
		this.address=address;
		this.phoneNumber=phoneNumber;
	}	
    
    // Getters and Setters
 
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<BookAuthor> getFavouriteBookAuthors() {
		return favouriteBookAuthors;
	}

	public void setFavouriteBookAuthors(List<BookAuthor> favouriteBookAuthors) {
		this.favouriteBookAuthors = favouriteBookAuthors;
	}

	public List<BookCategory> getFavouriteBookCategories() {
		return favouriteBookCategories;
	}

	public void setFavouriteBookCategories(List<BookCategory> favouriteBookCategories) {
		this.favouriteBookCategories = favouriteBookCategories;
	}

	public List<Book> getBookOffers() {
		return bookOffers;
	}

	public void setBookOffers(List<Book> bookOffers) {
		this.bookOffers = bookOffers;
	}
	
	public List<Book> getBookRequests() {
		return bookRequests;
	}

	public void setBookRequests(List<Book> bookRequests) {
		this.bookRequests = bookRequests;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
