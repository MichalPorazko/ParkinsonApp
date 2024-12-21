package com.example.ParkinsonApp.Authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GenericViewModelFactory<T: ViewModel>(private val create: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(create().javaClass)) {
            @Suppress("UNCHECKED_CAST")
            return create() as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}