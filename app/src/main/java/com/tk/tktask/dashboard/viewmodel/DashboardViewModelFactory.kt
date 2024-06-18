package com.tk.tktask.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tk.tktask.dashboard.repository.DashboardRepository
import com.tk.tktask.login.repository.ILoginRepository

class DashboardViewModelFactory(
    private val repository: DashboardRepository,
    private val loginRepository: ILoginRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository, loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}