#include "Employee.h"

Employee::Employee(string name, Date birth, Address address, string phone)
{
	this->name = name;
	this->birth = birth;
	this->address = address;
	this->phone = phone;
}

string Employee::getName() const
{
	return this->name;
}

string Employee::getPhone() const
{
	return this->phone;
}

Date Employee::getBirth() const
{
	return this->birth;
}

Address Employee::getAddress() const
{
	return this->address;
}

void Employee::displayEmployee() const
{
	cout << "-------------------" << endl;
	cout << "Name: " << this->name << endl;
	cout << "Date of Birth: " << this->birth.toString() << endl;
	cout << "Phone number: " << this->phone << endl;
	cout << "Address: " << this->address.toString() << endl;
	cout << "-------------------" << endl;
}