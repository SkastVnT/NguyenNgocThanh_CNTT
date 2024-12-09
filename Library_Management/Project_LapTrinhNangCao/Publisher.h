#pragma once
#include <iostream>
#include <list>
#include "Book.h"

using namespace std;

class Publisher
{
public:
	Publisher() = default;
	Publisher(string name);
	Publisher(string name, Book book);
	Publisher(string name, list<Book> books);
	string getName() const;
	list<Book> getPublisherBooks() const;
	void displayPublisher() const;
	void displayPublisherBook() const;
	void detailPublisher() const;
	void addBookToPublisherBooks(const Book& book);
	void deleteBookFromPublisherBooks(const string& bookTitle);

private:
	string name;
	list<Book> books;
};

