#pragma once
#include <iostream>
#include <list>
#include "Book.h"

using namespace std;

class Genre
{
public:
	Genre() = default;
	Genre(string name);
	Genre(string name, Book book);
	Genre(string name, list<Book> books);
	string getName() const;
	list<Book> getGenreBooks() const;
	void displayGenre() const;
	void displayGenreBook() const;
	void detailGenre() const;
	void addBookToGenreBooks(const Book& book);
	void deleteBookFromGenreBooks(const string& bookTitle);

private:
	string name;
	list<Book> books;
};

