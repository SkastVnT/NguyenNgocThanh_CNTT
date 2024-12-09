#include "Menu.h"
#include <string>

void Menu::loadMainMenu()
{
    Library library;

    /////Books, Authors, Publishers, Genres Data/////
    Book b1 = Book("The Alchemist", "Paulo Coelho", "DukMinh", "Dream", 1993, 5);
    Book b2 = Book("The Secret Language of Luck", "Gary Goldschneider", "DukMinh", "Dream", 2004, 5);

    library.addBook(b1);
    library.addBook(b2);
    /////////////////////////////////////////////////

    /////Members Data/////
    Member m1 = Member("Le Duc Minh", Date(2003, 8, 15), Address("Dong Nai", "Nha Trang", "Khanh Hoa"), "0000000000");
    Member m2 = Member("Tran Luong Duong Nhi", Date(2003, 12, 12), Address("Nha Duong Nhi", "Ninh Hoa", "Khanh Hoa"), "1111111111");
    Member m3 = Member("Nguyen Ngoc Thanh", Date(2003, 11, 11), Address("Nguyen Van Trang", "Q1", "Sai Gon"), "2222222222");
    Member m4 = Member("Pham Gia Khanh", Date(2003, 10, 10), Address("Thanh Thai", "Q1", "Sai Gon"), "3333333333");
    Member m5 = Member("Bui Tien Thinh", Date(2003, 9, 9), Address("Quang Trung", "Q12", "Sai Gon"), "4444444444");

    library.addMember(m1);
    library.addMember(m2);
    library.addMember(m3);
    library.addMember(m4);
    library.addMember(m5);
    //////////////////////

    /////Employees Data/////
    Employee e1 = Employee("Le Trung Nghi", Date(2003, 8, 15), Address("Dong Nai", "Nha Trang", "Khanh Hoa"), "55555555555");
    Employee e2 = Employee("Nguyen Thanh Hai", Date(2003, 1, 1), Address("Cao Thang", "Q3", "Sai Gon"), "6666666666");
    Employee e3 = Employee("Do Thuy Duong", Date(2003, 2, 2), Address("Nguyen Van Trang", "Q1", "Sai Gon"), "7777777777");
    Employee e4 = Employee("Lam Yen Nhi", Date(2003, 3, 3), Address("Thanh Thai", "Q10", "Sai Gon"), "8888888888");
    Employee e5 = Employee("Trinh Thanh Ha", Date(2003, 4, 4), Address("Quang Trung", "Q12", "Sai Gon"), "9999999999");

    library.addEmployee(e1);
    library.addEmployee(e2);
    library.addEmployee(e3);
    library.addEmployee(e4);
    library.addEmployee(e5);
    /////////////////////////

    /////Transactions Data/////
    Date current = Date(29, 11, 2023);

    Transaction t1 = Transaction(m1, b1, current, Date(30, 11, 2023));
    Transaction t2 = Transaction(m2, b1, current, Date(14, 12, 2023));
    Transaction t3 = Transaction(m3, b2, current, Date(29, 12, 2023));

    library.makeTransaction(t1);
    library.makeTransaction(t2);
    library.makeTransaction(t3);
    ////////////////////////////

    int choice = 0;
    do {
        system("cls"); //clear screen

        cout << "----------L I B R A R Y   M A N A G E M E N T   S Y S T E M----------" << endl;
        cout << "\t1. Book Management" << endl;
        cout << "\t2. Member Management" << endl;
        cout << "\t3. Transaction Management" << endl;
        cout << "\t4. Author Management" << endl;
        cout << "\t5. Genre Management" << endl;
        cout << "\t6. Publisher Management" << endl;
        cout << "\t7. Employee Management" << endl;
        cout << "\t8. Exit System" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            loadBookManagementMenu(library);
            break;
        case 2:
            loadMemberManagementMenu(library);
            break;
        case 3:
            loadTransactionManagementMenu(library);
            break;
        case 4:
            loadAuthorManagementMenu(library);
            break;
        case 5:
            loadGenreManagementMenu(library);
            break;
        case 6:
            loadPublisherManagementMenu(library);
            break;
        case 7:
            loadEmployeeManagementMenu(library);
            break;
        case 8:
            cout << "System end!!";
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 8);
}

//Done
void Menu::loadBookManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------B O O K   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of books" << endl;
        cout << "\t2. Search book by title or author" << endl;
        cout << "\t3. Add book" << endl;
        cout << "\t4. Delete book by title" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listBooks();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter title or author of book that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchBook(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string title;
            string author;
            string publisher;
            string genre;
            int year;
            int copies;

            cin.get();
            cout << "Enter book's title: ";
            getline(cin, title);
            cout << "Enter book's author: ";
            getline(cin, author);
            cout << "Enter book's publisher: ";
            getline(cin, publisher);
            cout << "Enter book's genre: ";
            getline(cin, genre);
            cout << "Enter book's year: ";
            cin >> year;
            cout << "Enter book's copies: ";
            cin >> copies;

            library.addBook(Book(title, author, publisher, genre, year, copies));
            
            cin.get();
            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter title of book that you want to delete: ";
            cin.get();
            getline(cin, searchKey);

            library.deleteBook(searchKey);

            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadAuthorManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------A U T H O R   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of authors" << endl;
        cout << "\t2. Search author by name" << endl;
        cout << "\t3. Add author" << endl;
        cout << "\t4. Delete author" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listAuthors();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter name of author that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchAuthor(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string name;

            cout << "Enter author's name: ";
            cin.get();
            getline(cin, name);

            library.addAuthor(Author(name));

            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter name of author that you want to delete: ";
            cin.get();
            getline(cin, searchKey);

            library.deleteAuthor(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadPublisherManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------P U B L I S H E R   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of publishers" << endl;
        cout << "\t2. Search publisher by name" << endl;
        cout << "\t3. Add publisher" << endl;
        cout << "\t4. Delete publisher" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listPublishers();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter name of publisher that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchPublisher(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string name;

            cout << "Enter publisher's name: ";
            cin.get();
            getline(cin, name);

            library.addPublisher(Publisher(name));

            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter name of publisher that you want to delete: ";
            cin.get();
            getline(cin, searchKey);

            library.deletePublisher(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadGenreManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------G E N R E   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of genres" << endl;
        cout << "\t2. Search genre by name" << endl;
        cout << "\t3. Add genre" << endl;
        cout << "\t4. Delete genre" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listGenres();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter name of genre that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchGenre(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string name;

            cout << "Enter genre's name: ";
            cin.get();
            getline(cin, name);

            library.addGenre(Genre(name));

            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter name of genre that you want to delete: ";
            cin.get();
            getline(cin, searchKey);

            library.deleteGenre(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadMemberManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------M E M B E R   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of members" << endl;
        cout << "\t2. Search member by name" << endl;
        cout << "\t3. Add member" << endl;
        cout << "\t4. Delete member by name" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listMembers();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter member's name that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchMember(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string name;
            string phone;
            //birth
            int birthDay;
            int birthMonth;
            int birthYear;
            //address
            string street;
            string district;
            string city;

            cin.get();
            cout << "Enter member's name: ";
            getline(cin, name);
            cout << "Enter phone number: ";
            cin >> phone;
            cout << "Enter member's birth" << endl;
            cout << "Enter member's birth day: ";
            cin >> birthDay;
            cout << "Enter member's birth month: ";
            cin >> birthMonth;
            cout << "Enter member's birth year: ";
            cin >> birthYear;
            cout << "Enter member's address" << endl;
            cout << "Enter street: ";
            cin.get();
            getline(cin, street);
            cout << "Enter district: ";
            getline(cin, district);
            cout << "Enter city: ";
            getline(cin, city);

            library.addMember(Member(name, Date(birthDay, birthMonth, birthYear), Address(street, district, city), phone));

            cin.get();
            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter member's name that you want to delete: ";
            cin.get();
            getline(cin, searchKey);
            library.deleteMember(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadEmployeeManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------M E M B E R   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of employees" << endl;
        cout << "\t2. Search employee by name" << endl;
        cout << "\t3. Add employee" << endl;
        cout << "\t4. Delete employee by name" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listEmployees();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter employee's name that you want to find: ";
            cin.get();
            getline(cin, searchKey);
            library.searchEmployee(searchKey);
            cin.get();
            break;
        }
        case 3:
        {
            string name;
            string phone;
            //birth
            int birthDay;
            int birthMonth;
            int birthYear;
            //address
            string street;
            string district;
            string city;

            cin.get();
            cout << "Enter employee's name: ";
            getline(cin, name);
            cout << "Enter phone number: ";
            cin >> phone;
            cout << "Enter employee's birth" << endl;
            cout << "Enter employee's birth day: ";
            cin >> birthDay;
            cout << "Enter employee's birth month: ";
            cin >> birthMonth;
            cout << "Enter employee's birth year: ";
            cin >> birthYear;
            cout << "Enter employee's address" << endl;
            cout << "Enter street: ";
            cin.get();
            getline(cin, street);
            cout << "Enter district: ";
            getline(cin, district);
            cout << "Enter city: ";
            getline(cin, city);

            library.addEmployee(Employee(name, Date(birthDay, birthMonth, birthYear), Address(street, district, city), phone));

            cin.get();
            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter employee's name that you want to delete: ";
            cin.get();
            getline(cin, searchKey);
            library.deleteEmployee(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}

void Menu::loadTransactionManagementMenu(Library& library)
{
    int choice = 0;
    do {
        system("cls");

        cout << "----------T R A N S A C T I O N   M A N A G E M E N T----------" << endl;
        cout << "\t1. List of transactions" << endl;
        cout << "\t2. Search transaction by member's phone number" << endl;
        cout << "\t3. Create transaction" << endl;
        cout << "\t4. Delete transaction by member's phone number" << endl;
        cout << "\t5. Go back" << endl;

        cout << "Choose your choice: ";
        cin >> choice;

        switch (choice) {
        case 1:
            library.listTransaction();
            cin.get();
            cin.get();
            break;
        case 2:
        {
            string searchKey = "";
            cout << "Enter member's phone number that you want to find their transaction: ";
            cin >> searchKey;
            library.searchTransaction(searchKey);
            cin.get();
            cin.get();
            break;
        }
        case 3:
        {
            Book book;
            Member member;

            string phone;
            string bookTitle;
            string bookAuthor;
            string bookPublisher;

            cout << "Enter member's phone number: ";
            cin >> phone;
            cout << "Enter book's detail" << endl;
            cout << "Enter book's title: ";
            cin.get();
            getline(cin, bookTitle);
            cout << "Enter book's author: ";
            getline(cin, bookAuthor);
            cout << "Enter book's publisher: ";
            getline(cin, bookPublisher);

            bool rbook = library.findBook(book, bookTitle, bookAuthor, bookPublisher);
            bool rmember = library.findMember(member, phone);

            if (rbook == true && rmember == true) {
                if (member.getIsBorrow() == false && book.getCopies() > 0) {
                    int startDay, startMonth, startYear;
                    int dueDay, dueMonth, dueYear;

                    cout << "Enter date of transaction" << endl;
                    cout << "Enter day: ";
                    cin >> startDay;
                    cout << "Enter month: ";
                    cin >> startMonth;
                    cout << "Enteryear: ";
                    cin >> startYear;

                    cout << "Enter due date of transaction" << endl;
                    cout << "Enter day: ";
                    cin >> dueDay;
                    cout << "Enter month: ";
                    cin >> dueMonth;
                    cout << "Enter year: ";
                    cin >> dueYear;

                    library.makeTransaction(Transaction(member, book,
                        Date(startYear, startMonth, startDay),
                        Date(dueYear, dueMonth, dueDay)));
                }
                else {
                    if (member.getIsBorrow() == true) {
                        cout << "This member have already borrowed a book." << endl;
                        library.searchTransaction(member.getPhone());
                    }
                    if (book.getCopies() == 0) {
                        cout << "This book is out of copies." << endl;
                        library.searchBook(book.getTitle());
                    }
                }
            }
            else {
                cout << "Membes's phone number or book's detail is not correct.";
            }

            cin.get();
            cin.get();
            break;
        }
        case 4:
        {
            string searchKey = "";
            cout << "Enter member's phone number that you want to delete the transaction: ";
            cin.get();
            getline(cin, searchKey);
            library.removeTransaction(searchKey);
            cin.get();
            break;
        }
        case 5:
            cout << "Going back to library management.....";
            cin.get();
            cin.get();
            break;
        default:
            cout << "Your choice is not valiable! Please choose again.";
            cin.get();
            cin.get();
            break;
        }
    } while (choice != 5);
}