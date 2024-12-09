#pragma once
#include <iostream>
#include "Library.h"

using namespace std;

class Menu
{
public:
	void loadMainMenu();
	void loadBookManagementMenu(Library& library);
	void loadMemberManagementMenu(Library& library);
	void loadTransactionManagementMenu(Library& library);
	void loadAuthorManagementMenu(Library& library);
	void loadGenreManagementMenu(Library& library);
	void loadPublisherManagementMenu(Library& library);
	void loadEmployeeManagementMenu(Library& library);
};