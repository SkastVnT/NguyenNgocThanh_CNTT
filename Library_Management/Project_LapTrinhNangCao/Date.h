#pragma once
#include <iostream>

using namespace std;

class Date
{
public:
	Date() = default;
	Date(int year, int month, int day);
	int getDay() const;
	int getMonth() const;
	int getYear() const;
	string toString() const;

private:
	int day;
	int month;
	int year;
};

