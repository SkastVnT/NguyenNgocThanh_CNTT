#pragma once
#include <iostream>
#include "Member.h"
#include "Book.h"
#include "Date.h"

using namespace std;

class Transaction
{
public:
    Transaction() = default;
    Transaction(Member member, Book book, Date dateOfTransaction, Date dueDate);
    Book& getBook();
    Member& getMember();
    Date getDateOfTransaction() const;
    Date getDueDate() const;
    void displayTransactionDetails() const;

private:
    Member member;
    Book book;
    Date dateOfTransaction;
    Date dueDate;
};