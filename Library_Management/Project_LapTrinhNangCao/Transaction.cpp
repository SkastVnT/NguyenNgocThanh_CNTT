#include "Transaction.h"

Transaction::Transaction(Member member, Book book, Date dateOfTransaction, Date dueDate)
{
	this->member = member;
	this->book = book;
	this->dateOfTransaction = dateOfTransaction;
	this->dueDate = dueDate;
}

Book& Transaction::getBook()
{
	return this->book;
}

Member& Transaction::getMember()
{
	return this->member;
}

Date Transaction::getDateOfTransaction() const
{
	return this->dateOfTransaction;
}

Date Transaction::getDueDate() const
{
	return this->dueDate;
}

void Transaction::displayTransactionDetails() const
{
	cout << endl;
	cout << "-------------------" << endl;
	cout << "Member: " << this->member.getName() << "\t" << this->member.getPhone() << endl;
	cout << "Book borrow: " << endl;
	this->book.displayBook();
	cout << "Date of transaction: " << this->dateOfTransaction.toString() << endl;
	cout << "Due date of transaction: " << this->dueDate.toString() << endl;
	cout << "-------------------" << endl;
}