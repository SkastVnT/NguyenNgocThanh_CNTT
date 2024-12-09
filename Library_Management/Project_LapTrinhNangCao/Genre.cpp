#include "Genre.h"

Genre::Genre(string name)
{
	this->name = name;
}

Genre::Genre(string name, Book book)
{
	this->name = name;
	this->books.push_back(book);
}

Genre::Genre(string name, list<Book> books)
{
	this->name = name;
	this->books = books;
}

string Genre::getName() const
{
	return this->name;
}

list<Book> Genre::getGenreBooks() const
{
	return this->books;
}

void Genre::displayGenre() const
{
	cout << "Name: " << this->name << endl;
}

void Genre::displayGenreBook() const
{
	for (const auto& book : this->books) {
		cout << endl;
		book.displayBook();
	}
}

void Genre::detailGenre() const
{
	cout << "-------------------" << endl;
	this->displayGenre();
	cout << "List of genre's books:";
	this->displayGenreBook();
	cout << "-------------------" << endl;
}

void Genre::addBookToGenreBooks(const Book& book)
{
	this->books.push_back(book);
}

void Genre::deleteBookFromGenreBooks(const string& bookTitle)
{
	auto it = remove_if(this->books.begin(), this->books.end(),
		[bookTitle](const Book& book) {return book.getTitle() == bookTitle;});
	this->books.erase(it, books.end());
}