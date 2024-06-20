package app.services;

import java.util.List;

import app.model.Book;
import app.model.UserProfile;

public interface RecommendationsStrategy {
    List<Book> recommendBooks(UserProfile userProfile);
}