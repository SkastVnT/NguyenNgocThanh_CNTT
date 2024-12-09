#include "Address.h"

Address::Address(string street, string district, string city)
{
	this->street = street;
	this->district = district;
	this->city = city;
}

string Address::getStreet() const
{
	return this->street;
}

string Address::getDistrict() const
{
	return this->district;
}

string Address::getCity() const
{
	return this->city;
}

string Address::toString() const
{
	return this->street + ", " + this->district + ", " + this->city;
}