package com.muthuvarshith.nammaraste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muthuvarshith.nammaraste.model.User
import com.muthuvarshith.nammaraste.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.getUserByUsername(username)
                if (user != null && user.password == password) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Invalid username or password")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun register(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Fields cannot be empty")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val existing = repository.getUserByUsername(username)
                if (existing != null) {
                    _authState.value = AuthState.Error("Username already exists")
                } else {
                    val newUser = User(
                        username = username,
                        password = password
                    )
                    repository.register(newUser)
                    _authState.value = AuthState.Success
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
