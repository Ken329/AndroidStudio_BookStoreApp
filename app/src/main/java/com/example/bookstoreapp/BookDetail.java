package com.example.bookstoreapp;

public class BookDetail {
    String book_name, author, date, borrowed;

    public BookDetail(String book_name, String author, String date, String borrowed) {
        this.book_name = book_name;
        this.author = author;
        this.date = date;
        this.borrowed = borrowed;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
    }
}
