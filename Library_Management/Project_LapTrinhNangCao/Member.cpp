#include "Member.h"

Member::Member(string name, Date birth, Address address, string phone)
{
	this->name = name;
	this->birth = birth;
	this->address = address;
	this->phone = phone;
	this->isBorrow = false;
}

string Member::getName() const
{
	return this->name;
}

Date Member::getBirth() const
{
	return this->birth;
}

Address Member::getAddress() const
{
	return this->address;
}

string Member::getPhone() const
{
	return this->phone;
}

bool Member::getIsBorrow() const
{
	return this->isBorrow;
}
void Member::setIsBorrow(const bool& isBorrow) {
	this->isBorrow = isBorrow;
}

void Member::displayMember() const
{
	cout << endl;
	cout << "-------------------" << endl;
	cout << "Name: " << this->name << endl;
	cout << "Date of Birth: " << this->birth.toString() << endl;
	cout << "Phone number: " << this->phone << endl;
	cout << "Address: " << this->address.toString() << endl;
	if (this->isBorrow == false){
		cout << "Borrow status: can borrow book";
	}
	else {
		cout << "Borrow status: already borrow a book";
	}
	cout << endl;
	cout << "-------------------" << endl;
}