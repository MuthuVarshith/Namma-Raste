package com.muthuvarshith.nammaraste.repository

import com.muthuvarshith.nammaraste.data.UserDao
import com.muthuvarshith.nammaraste.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: User) = userDao.insertUser(user)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
}
