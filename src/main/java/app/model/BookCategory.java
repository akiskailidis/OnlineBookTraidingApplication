package app.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="book_categories")
public class BookCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="categoryId")
    private int categoryId;

	@Column(name="name")
    private String name;

    
	@OneToMany
    private List<Book> books;

    // Constructors
	
    public BookCategory() {
    	
    }

    public BookCategory(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryid) {
        this.categoryId = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
