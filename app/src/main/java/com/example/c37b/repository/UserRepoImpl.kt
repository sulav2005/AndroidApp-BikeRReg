package com.example.c37b.repository

import com.example.c37b.db.DatabaseHelper
import com.example.c37b.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepoImpl : UserRepo {

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "SELECT * FROM users WHERE email = ? AND password = ?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, email)
                    statement.setString(2, password)
                    val resultSet = statement.executeQuery()
                    
                    withContext(Dispatchers.Main) {
                        if (resultSet.next()) {
                            callback(true, "Login success")
                        } else {
                            callback(false, "Invalid email or password")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        callback(false, "Database error: ${e.message}")
                    }
                } finally {
                    connection.close()
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback(false, "Database connection failed")
                }
            }
        }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        // In a real scenario, you'd check if user exists in DB first
        val userId = java.util.UUID.randomUUID().toString()
        callback(true, "Registration success", userId)
    }

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "INSERT INTO users (id, first_name, last_name, gender, dob, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, userId)
                    statement.setString(2, model.firstName)
                    statement.setString(3, model.lastName)
                    statement.setString(4, model.gender)
                    statement.setString(5, model.dob)
                    statement.setString(6, model.email)
                    statement.setString(7, model.password)
                    val result = statement.executeUpdate()
                    
                    withContext(Dispatchers.Main) {
                        if (result > 0) {
                            callback(true, "User added successfully")
                        } else {
                            callback(false, "Failed to add user")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        callback(false, "Database error: ${e.message}")
                    }
                } finally {
                    connection.close()
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback(false, "Database connection failed")
                }
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        // Implementation for PostgreSQL
        callback(true, "Reset logic not implemented for SQL yet")
    }

    override fun deleteAccount(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "DELETE FROM users WHERE id = ?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, userId)
                    val result = statement.executeUpdate()
                    withContext(Dispatchers.Main) {
                        if (result > 0) callback(true, "Account deleted")
                        else callback(false, "User not found")
                    }
                } finally {
                    connection.close()
                }
            }
        }
    }

    override fun editProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "UPDATE users SET first_name=?, last_name=?, gender=?, dob=? WHERE id=?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, model.firstName)
                    statement.setString(2, model.lastName)
                    statement.setString(3, model.gender)
                    statement.setString(4, model.dob)
                    statement.setString(5, userId)
                    val result = statement.executeUpdate()
                    withContext(Dispatchers.Main) {
                        if (result > 0) callback(true, "Profile updated")
                        else callback(false, "User not found")
                    }
                } finally {
                    connection.close()
                }
            }
        }
    }

    override fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "SELECT * FROM users WHERE id = ?"
                    val statement = connection.prepareStatement(query)
                    statement.setString(1, userId)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        val user = UserModel(
                            id = resultSet.getString("id"),
                            firstName = resultSet.getString("first_name"),
                            lastName = resultSet.getString("last_name"),
                            gender = resultSet.getString("gender"),
                            dob = resultSet.getString("dob"),
                            email = resultSet.getString("email"),
                            password = resultSet.getString("password")
                        )
                        withContext(Dispatchers.Main) {
                            callback(true, "User found", user)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            callback(false, "User not found", null)
                        }
                    }
                } finally {
                    connection.close()
                }
            }
        }
    }

    override fun getAllUser(callback: (Boolean, String, List<UserModel>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection = DatabaseHelper.getConnection()
            if (connection != null) {
                try {
                    val query = "SELECT * FROM users"
                    val statement = connection.prepareStatement(query)
                    val resultSet = statement.executeQuery()
                    val userList = mutableListOf<UserModel>()
                    while (resultSet.next()) {
                        userList.add(UserModel(
                            id = resultSet.getString("id"),
                            firstName = resultSet.getString("first_name"),
                            lastName = resultSet.getString("last_name"),
                            gender = resultSet.getString("gender"),
                            dob = resultSet.getString("dob"),
                            email = resultSet.getString("email"),
                            password = resultSet.getString("password")
                        ))
                    }
                    withContext(Dispatchers.Main) {
                        callback(true, "Users fetched", userList)
                    }
                } finally {
                    connection.close()
                }
            }
        }
    }
}
