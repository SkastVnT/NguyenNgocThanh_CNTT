#include "Library.h"

//////////Books Management//////////
void Library::addBook(const Book& book)
{
    bool isAuthor = false;
    bool isPublisher = false;
    bool isGenre = false;

    //Add books in library
	this->books.push_back(book);
    cout << "Book added successfully!";

    //Add books in author
    for (auto& author : this->authors) {
        if (author.getName() == book.getAuthor()) {
            author.addBookToAuthorBooks(book);
            isAuthor = true;
        }
    }
    if (isAuthor == false) {
        this->addAuthor(Author(book.getAuthor(), book));
    }

    //Add books in publisher
    for (auto& publisher : this->publishers) {
        if (publisher.getName() == book.getPublisher()) {
            publisher.addBookToPublisherBooks(book);
            isPublisher = true;
        }
    }
    if (isPublisher == false) {
        this->addPublisher(Publisher(book.getPublisher(), book));
    }

    //Add books in genre
    for (auto& genre : this->genres) {
        if (genre.getName() == book.getGenre()) {
            genre.addBookToGenreBooks(book);
            isGenre = true;
        }
    }
    if (isGenre == false) {
        this->addGenre(Genre(book.getGenre(), book));
    }
}
void Library::listBooks() const
{
    system("cls");
    cout << "----------L I S T   O F   B O O K S----------" << endl;
    for (const auto &book : this->books) {
        book.displayBook();
        cout << endl;
    }
}
void Library::searchBook(const string& searchKey) const
{
    bool isBook = false;
    for (const auto& book : this->books) {
        if (book.getTitle() == searchKey || book.getAuthor() == searchKey) {
            book.detailBook();
            isBook = true;
        }
    }
    if (isBook== false) {
        cout << "Can't find book's title or author with name " + searchKey;
    }
}
bool Library::findBook(Book& fbook, const string& title, const string& author, const string& publisher) const
{
    for (const auto& book : this->books) {
        if (book.getTitle() == title && book.getAuthor() == author && book.getPublisher() == publisher) {
            fbook = book;
            return true;
        }
    }
    return false;
}
void Library::deleteBook(const string& searchKey)
{
    //remove_if xet dieu kien trong khoang can xet, neu tim ra phan tu co dieu kien muon xet thi dua phan tu do ra cuoi
    //Lambda Function [bookTitle](const Book &book) {return book.getTitle() == bookTitle;}

    //Delete books in library
    auto itBook = remove_if(this->books.begin(), this->books.end(),
        [searchKey](const Book& book) {return book.getTitle() == searchKey;});
    this->books.erase(itBook, books.end());
;
    //Delete books in author
    for (auto& author : this->authors) {
        for (auto& book : author.getAuthorBooks()) {
            if (book.getTitle() == searchKey) {
                author.deleteBookFromAuthorBooks(searchKey);
                break;
            }
        }
    }

    //Delete books in publisher
    for (auto& publisher : this->publishers) {
        for (auto& book : publisher.getPublisherBooks()) {
            if (book.getTitle() == searchKey) {
                publisher.deleteBookFromPublisherBooks(searchKey);
                break;
            }
        }
    }
    
    //Delete books in genre
    for (auto& genre : this->genres) {
        for (auto& book : genre.getGenreBooks()) {
            if (book.getTitle() == searchKey) {
                genre.deleteBookFromGenreBooks(searchKey);
                break;
            }
        }
    }

    cout << "Deleted successfully";
}


//////////Authors Management//////////
void Library::addAuthor(const Author& author)
{
    this->authors.push_back(author);
    cout << "Author added successfully!";
}
void Library::listAuthors() const
{
    system("cls");
    cout << "----------L I S T   O F   A U T H O R S----------" << endl;
    for (const auto& author : this->authors) {
        author.displayAuthor();
        cout << endl;
    }
}
void Library::searchAuthor(const string& searchKey) const
{
    bool isAuthor = false;
    for (const auto& author : this->authors) {
        if (author.getName() == searchKey) {
            author.detailAuthor();
            cout << endl;
            isAuthor = true;
        }
    }
    if (isAuthor == false) {
        cout << "Can't find author with name " + searchKey;
    }
}
void Library::deleteAuthor(const string& searchKey)
{
    auto itAuthor = remove_if(this->authors.begin(), this->authors.end(),
        [searchKey](const Author& author) {return author.getName() == searchKey;});
    this->authors.erase(itAuthor, authors.end());

    auto itBook = remove_if(this->books.begin(), this->books.end(),
        [searchKey](const Book& book) {return book.getAuthor() == searchKey;});
    this->books.erase(itBook, books.end());

    cout << "Deleted successfully";
}


//////////Publishers Management//////////
void Library::addPublisher(const Publisher& publisher)
{
    this->publishers.push_back(publisher);
    cout << "Publisher added successfully!";
}
void Library::listPublishers() const
{
    system("cls");
    cout << "----------L I S T   O F   P U B L I S H E R----------" << endl;
    for (const auto& publisher : this->publishers) {
        publisher.displayPublisher();
        cout << endl;
    }
}
void Library::searchPublisher(const string& searchKey) const
{
    bool isPublisher = false;
    for (const auto& publisher : this->publishers) {
        if (publisher.getName() == searchKey) {
            publisher.detailPublisher();
            cout << endl;
            isPublisher = true;
        }
    }
    if (isPublisher == false) {
        cout << "Can't find publisher with name " + searchKey;
    }
}
void Library::deletePublisher(const string& searchKey)
{
    auto itPublisher = remove_if(this->publishers.begin(), this->publishers.end(),
        [searchKey](const Publisher& publisher) {return publisher.getName() == searchKey;});
    this->publishers.erase(itPublisher, publishers.end());

    auto itBook = remove_if(this->books.begin(), this->books.end(),
        [searchKey](const Book& book) {return book.getPublisher() == searchKey;});
    this->books.erase(itBook, books.end());

    cout << "Deleted successfully";
}


//////////Genres Management//////////
void Library::addGenre(const Genre& genre)
{
    this->genres.push_back(genre);
    cout << "Genre added successfully!";
}
void Library::listGenres() const
{
    system("cls");
    cout << "----------L I S T   O F   G E R N S----------" << endl;
    for (const auto& genre : this->genres) {
        genre.displayGenre();
        cout << endl;
    }
}
void Library::searchGenre(const string& searchKey) const
{
    bool isGenre = false;
    for (const auto& genre : this->genres) {
        if (genre.getName() == searchKey) {
            genre.detailGenre();
            cout << endl;
            isGenre = true;
        }
    }
    if (isGenre == false) {
        cout << "Can't find genre with name " + searchKey;
    }
}
void Library::deleteGenre(const string& searchKey)
{
    auto itGenre = remove_if(this->genres.begin(), this->genres.end(),
        [searchKey](const Genre& genre) {return genre.getName() == searchKey;});
    this->genres.erase(itGenre, genres.end());

    auto itBook = remove_if(this->books.begin(), this->books.end(),
        [searchKey](const Book& book) {return book.getGenre() == searchKey;});
    this->books.erase(itBook, books.end());

    cout << "Deleted successfully";
}


//////////Members Management//////////
void Library::addMember(const Member& member)
{
    this->members.push_back(member);
    cout << "Member added successfully!";
}
void Library::listMembers() const
{
    system("cls");
    cout << "----------L I S T   O F   M E M B E R S----------" << endl;
    for (const auto& member : this->members) {
        member.displayMember();
    }
}
void Library::searchMember(const string& searchKey) const
{
    for (const auto& member : this->members) {
        if (member.getName() == searchKey) {
            member.displayMember();
            cout << endl;
        }
    }
}
bool Library::findMember(Member& fmember, const string& phone) const
{
    for (const auto& member : this->members) {
        if (member.getPhone() == phone) {
            fmember = member;
            return true;
        }
    }
    return false;
}
void Library::deleteMember(const string& searchKey)
{
    auto it = remove_if(this->members.begin(), this->members.end(),
        [searchKey](const Member& member) {return member.getName() == searchKey;});
    this->members.erase(it, members.end());

    cout << "Deleted successfully";
}


//////////Employees Management//////////
void Library::addEmployee(const Employee& employee)
{
    this->employees.push_back(employee);
    cout << "Employee added successfully!";
}
void Library::listEmployees() const
{
    system("cls");
    cout << "----------L I S T   O F   E M P L O Y E E----------" << endl;
    for (const auto& employee : this->employees) {
        employee.displayEmployee();
    }
}
void Library::searchEmployee(const string& searchKey) const
{
    for (const auto& employee : this->employees) {
        if (employee.getName() == searchKey) {
            employee.displayEmployee();
            cout << endl;
        }
    }
}
void Library::deleteEmployee(const string& searchKey)
{
    auto it = remove_if(this->employees.begin(), this->employees.end(),
        [searchKey](const Employee& employee) {return employee.getName() == searchKey;});
    this->employees.erase(it, employees.end());

    cout << "Deleted successfully";
}


//////////Transactions management//////////
void Library::makeTransaction(Transaction transaction)
{
    transaction.displayTransactionDetails();
    transaction.getMember().setIsBorrow(true);
    cout << transaction.getMember().getIsBorrow();
    transaction.getBook().reduceCopies();
    cout << transaction.getBook().getCopies();
    this->transactions.push_back(transaction);
}
void Library::listTransaction() const
{
    system("cls");
    cout << "----------L I S T   O F   T R A N S A C T I O N----------" << endl;
    for (const auto& transaction : this->transactions) {
        transaction.displayTransactionDetails();
    }
}
void Library::searchTransaction(const string& searchKey)
{
    for (auto& transaction : this->transactions) {
        if (transaction.getMember().getPhone() == searchKey) {
            transaction.displayTransactionDetails();
            cout << endl;
        }
    }
}
void Library::removeTransaction(const string& searchKey)
{
    for (auto& transaction : this->transactions) {
        if (transaction.getMember().getPhone() == searchKey) {
            transaction.getMember().setIsBorrow(false);
            cout << transaction.getMember().getIsBorrow();
            transaction.getBook().addCopies();
            cout << transaction.getBook().getCopies();
        }
    }

    auto it = remove_if(this->transactions.begin(), this->transactions.end(),
        [searchKey](Transaction& transaction) {return transaction.getMember().getPhone() == searchKey;});
    this->transactions.erase(it, transactions.end());

    cout << "Deleted successfully";
}