package com.example.clothstoreapp.Helper

class UserRoleManager {
    object UserRoleManager {
        const val ROLE_ADMIN = "admin"
        const val ROLE_CUSTOMER = "customer"

        fun isAdmin(role: String): Boolean = role == ROLE_ADMIN
        fun isCustomer(role: String): Boolean = role == ROLE_CUSTOMER
    }
}