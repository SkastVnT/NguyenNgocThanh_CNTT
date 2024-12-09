#include "Date.h"
#include <string>

Date::Date(int year, int month, int day)
{
	this->year = year;
	this->month = month;
	this->day = day;
}

int Date::getDay() const
{
	return this->day;
}

int Date::getMonth() const
{
	return this->month;
}

int Date::getYear() const
{
	return this->year;
}

string Date::toString() const
{
	return to_string(this->year) + "-" + to_string(this->month) + "-" + to_string(this->day);
}