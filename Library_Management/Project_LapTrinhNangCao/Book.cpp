#include "Book.h"

Book::Book(string title, string author, string publisher, string genre, int year, int copies)
{
	this->title = title;
	this->author = author;
	this->publisher = publisher;
	this->genre = genre;
	this->year = year;
	this->copies = copies;
}

string Book::getTitle() const
{
	return this->title;
}

string Book::getAuthor() const
{
	return this->author;
}

string Book::getPublisher() const
{
	return this->publisher;
}

string Book::getGenre() const
{
	return this->genre;
}

int Book::getYear() const
{
	return this->year;
}

int Book::getCopies() const
{
	return this->copies;
}

void Book::addCopies()
{
	this->copies += 1;
}

void Book::reduceCopies()
{
	this->copies -= 1;
}

void Book::displayBook() const
{
	cout << "-------------------" << endl;
	cout << "Title: " << this->title << endl;
	cout << "Author: " << this->author << endl;
	cout << "Year: " << this->year << endl;
	cout << "-------------------" << endl;
}

void Book::detailBook() const
{
	cout << "-------------------" << endl;
	cout << "Title: " << this->title << endl;
	cout << "Author: " << this->author << endl;
	cout << "Year: " << this->year << endl;
	cout << "Genre: " << this->genre << endl;
	cout << "Publisher: " << this->publisher << endl;
	cout << "Copies: " << this->copies << endl;
	cout << "-------------------" << endl;
}