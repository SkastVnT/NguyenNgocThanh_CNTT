#include "Publisher.h"

Publisher::Publisher(string name)
{
	this->name = name;
}

Publisher::Publisher(string name, Book book)
{
	this->name = name;
	this->books.push_back(book);
}

Publisher::Publisher(string name, list<Book> books)
{
	this->name = name;
	this->books = books;
}

string Publisher::getName() const
{
	return this->name;
}

list<Book> Publisher::getPublisherBooks() const
{
	return this->books;
}

void Publisher::displayPublisher() const
{
	cout << "Name: " << this->name << endl;
}

void Publisher::displayPublisherBook() const
{
	for (const auto& book : this->books) {
		cout << endl;
		book.displayBook();
	}
}

void Publisher::detailPublisher() const
{
	cout << "-------------------" << endl;
	this->displayPublisher();
	cout << "List of publisher's books:";
	this->displayPublisherBook();
	cout << "-------------------" << endl;
}

void Publisher::addBookToPublisherBooks(const Book& book)
{
	this->books.push_back(book);
}

void Publisher::deleteBookFromPublisherBooks(const string& bookTitle)
{
	auto it = remove_if(this->books.begin(), this->books.end(),
		[bookTitle](const Book& book) {return book.getTitle() == bookTitle;});
	this->books.erase(it, books.end());
}