#pragma once
#include <iostream>

using namespace std;

class Address
{
public:
	Address() = default;
	Address(string street, string district, string city);
	string getStreet() const;
	string getDistrict() const;
	string getCity() const;
	string toString() const;

private:
	string street;
	string district;
	string city;
};

