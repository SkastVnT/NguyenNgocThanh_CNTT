#pragma once
#include <iostream>
#include <list>
#include "Book.h"

using namespace std;

class Author
{
public:
	Author() = default;
	Author(string name);
	Author(string name, Book book);
	Author(string name, list<Book> books);
	string getName() const;
	list<Book> getAuthorBooks() const;
	void displayAuthor() const;
	void displayAuthorBook() const;
	void detailAuthor() const;
	void addBookToAuthorBooks(const Book& book);
	void deleteBookFromAuthorBooks(const string& bookTitle);

private:
	string name;
	list<Book> books;
};

