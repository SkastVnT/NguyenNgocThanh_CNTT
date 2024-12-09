#include "Author.h"

Author::Author(string name)
{
	this->name = name;
}

Author::Author(string name, Book book)
{
	this->name = name;
	this->books.push_back(book);
}

Author::Author(string name, list<Book> books)
{
	this->name = name;
	this->books = books;
}


string Author::getName() const
{
	return this->name;
}

list<Book> Author::getAuthorBooks() const
{
	return this->books;
}

void Author::displayAuthor() const
{
	cout << "Name: " << this->name << endl;
}

void Author::displayAuthorBook() const
{
	for (const auto& book : this->books) {
		cout << endl;
		book.displayBook();
	}
}

void Author::detailAuthor() const
{
	cout << "-------------------" << endl;
	this->displayAuthor();
	cout << "List of author's books:";
	this->displayAuthorBook();
	cout << "-------------------" << endl;
}

void Author::addBookToAuthorBooks(const Book& book)
{
	this->books.push_back(book);
}

void Author::deleteBookFromAuthorBooks(const string& bookTitle)
{
	auto it = remove_if(this->books.begin(), this->books.end(),
		[bookTitle](const Book& book) {return book.getTitle() == bookTitle;});
	this->books.erase(it, books.end());
}