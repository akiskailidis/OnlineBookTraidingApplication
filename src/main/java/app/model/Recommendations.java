package app.model;

import java.util.List;

public class Recommendations {
    private List<Book> recomendedBooks;

    public List<Book> getRecomendedBooks() {
        return recomendedBooks;
    }

    public void setRecomendedBooks(List<Book> recomendedBooks) {
        this.recomendedBooks = recomendedBooks;
    }
}
