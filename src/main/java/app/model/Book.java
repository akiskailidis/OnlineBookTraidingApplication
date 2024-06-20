package app.model;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="books")
public class Book {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bookId")
    private int bookId;

	@Column(name="title")
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "author")
    private BookAuthor author;  
    
    @ManyToOne
    @JoinColumn(name = "bookCategory")
    private BookCategory bookCategory;

    @ManyToMany
    private List<BookAuthor> bookAuthors;

    @ManyToMany
    private List<UserProfile> requestingUsers;

    // Constructors
    public Book() {
    	
    }
    
    public Book(int bookId, String title, List<BookAuthor> bookAuthors, BookCategory bookCategory,List<UserProfile> requestingUsers) {
        this.bookId = bookId;
        this.title = title;
        this.bookAuthors = bookAuthors;
        this.bookCategory = bookCategory;
        this.requestingUsers = requestingUsers;
    }
    
    // Getters and Setters
    
    public BookAuthor getAuthor() {
        return author;
    }

    public void setAuthor(BookAuthor author) {
        this.author = author;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public List<UserProfile> getRequestingUsers() {
        return requestingUsers;
    }

    public void setRequestingUsers(List<UserProfile> requestingUsers) {
        this.requestingUsers = requestingUsers;
    }
}
