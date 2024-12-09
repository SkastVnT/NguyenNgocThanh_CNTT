#pragma once
#include <iostream>
#include <list>
#include "Book.h"
#include "Author.h"
#include "Publisher.h"
#include "Genre.h"
#include "Member.h"
#include "Employee.h"
#include "Transaction.h"

using namespace std;

class Library
{
public:
	Library() = default;

	//////////Books management//////////
	void addBook(const Book& book);
	void listBooks() const;
	void searchBook(const string& searchKey) const;
	bool findBook(Book& book, const string& title, const string& author, const string& publisher) const;
	void deleteBook(const string& searchKey);

	//////////Authors management//////////
	void addAuthor(const Author& author);
	void listAuthors() const;
	void searchAuthor(const string& searchKey) const;
	void deleteAuthor(const string& searchKey);

	//////////Publishers management//////////
	void addPublisher(const Publisher& publisher);
	void listPublishers() const;
	void searchPublisher(const string& searchKey) const;
	void deletePublisher(const string& searchKey);

	//////////Genres management//////////
	void addGenre(const Genre& genre);
	void listGenres() const;
	void searchGenre(const string& searchKey) const;
	void deleteGenre(const string& searchKey);

	//////////Members management//////////
	void addMember(const Member& member);
	void listMembers() const;
	void searchMember(const string& searchKey) const;
	bool findMember(Member& fmember, const string& phone) const;
	void deleteMember(const string& searchKey);

	//////////Employees management//////////
	void addEmployee(const Employee& employee);
	void listEmployees() const;
	void searchEmployee(const string& searchKey) const;
	void deleteEmployee(const string& searchKey);

	//////////Transactions management//////////
	void makeTransaction(Transaction transaction);
	void listTransaction() const;
	void searchTransaction(const string& searchKey);
	void removeTransaction(const string& searchKey);

private:
	list<Book> books;
	list<Author> authors;
	list<Publisher> publishers;
	list<Genre> genres;
	list<Member> members;
	list<Employee> employees;
	list<Transaction> transactions;
};