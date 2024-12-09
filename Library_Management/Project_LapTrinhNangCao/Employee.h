#pragma once
#include <iostream>
#include "Address.h"
#include "Date.h"

using namespace std;

class Employee
{
public:
	Employee() = default;
	Employee(string name, Date birth, Address address, string phone);
	string getName() const;
	string getPhone() const;
	Date getBirth() const;
	Address getAddress() const;
	void displayEmployee() const;

private:
	string name;
	string phone;
	Date birth;
	Address address;
};

