#pragma once
#include <iostream>

using namespace std;

class Book
{
public:
	Book() = default;
	Book(string title, string author, string publisher, string genre, int year, int copies);
	string getTitle() const;
	string getAuthor() const;
	string getPublisher() const;
	string getGenre() const;
	int getYear() const;
	int getCopies() const;
	void addCopies();
	void reduceCopies();
	void displayBook() const;
	void detailBook() const;

private:
	string title;
	string author;
	string publisher;
	string genre;
	int year;
	int copies;
};