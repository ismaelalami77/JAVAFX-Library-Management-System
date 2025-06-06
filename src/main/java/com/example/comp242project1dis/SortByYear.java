package com.example.comp242project1dis;

import java.util.Comparator;

public class SortByYear implements Comparator<Book> {
    public int compare(Book b1, Book b2) {
        return Integer.compare(b2.getPublishedYear(), b1.getPublishedYear());
    }
}
