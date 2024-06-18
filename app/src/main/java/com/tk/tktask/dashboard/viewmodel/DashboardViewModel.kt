package com.tk.tktask.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tk.tktask.dashboard.repository.IDashboardRepository
import com.tk.tktask.login.repository.ILoginRepository
import com.tk.tktask.realmdb.enitity.LocationEntity
import com.tk.tktask.realmdb.enitity.UserEntity
import io.realm.Realm
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: IDashboardRepository,
    private val loginRepository: ILoginRepository
) : ViewModel() {
    private val _users = MutableLiveData<List<UserEntity>>()
    val users: LiveData<List<UserEntity>> = _users

    private val _currentUsers = MutableLiveData<UserEntity?>()
    val currentUsers: LiveData<UserEntity?> = _currentUsers

    private val _locations = MutableLiveData<List<LocationEntity>>()
    val locations: LiveData<List<LocationEntity>> = _locations

    private val _isWorkManageIsActive = MutableLiveData(false)
    val isWorkManageIsActive: LiveData<Boolean> = _isWorkManageIsActive


    private val _currentUserLocations = MutableLiveData<List<LocationEntity>>()
    val currentUserLocations: LiveData<List<LocationEntity>> = _currentUserLocations

    fun updateUser(data: UserEntity): Boolean {
        val userEntity = UserEntity().apply {
            this.email = data.email
            this.username = data.username
            this.password = data.password
        }
        val updateSuccess = loginRepository.loginUser(userEntity)
        if (updateSuccess) {
            loginRepository.logoutAllUsersExcept(userEntity)
        } else {
        }
        return updateSuccess
    }

    fun setCurrentUser(user: UserEntity) {
        _currentUsers.value = user
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            val mUserList = repository.getAllUsers().find { it.isCurrentUser }
            _currentUsers.postValue(mUserList)
        }
    }

    fun fetchNonCurrentUsers() {
        viewModelScope.launch {
            val usersFromRepo = repository.getAllUsers()
            _users.postValue(usersFromRepo.filterNot { it.isCurrentUser })
        }
    }

    fun updateLocation(location: LocationEntity) {
        viewModelScope.launch {
            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransaction { r ->
                val locationEntity = LocationEntity()
                locationEntity.email = location.email
                locationEntity.latitude = location.latitude
                locationEntity.longitude = location.longitude
                locationEntity.timestamp = System.currentTimeMillis()
                r.insertOrUpdate(locationEntity)
                val addItem = _currentUserLocations.value
                val newItem: ArrayList<LocationEntity> =
                    addItem?.let { ArrayList(it) } ?: arrayListOf()
                newItem.add(locationEntity)
                _currentUserLocations.postValue(newItem.toList())
            }
        }
    }


    fun fetchCurrentUserLocations() {
        viewModelScope.launch {
            val locations = repository.getCurrentUserLocations()
            _currentUserLocations.postValue(locations)
            Log.e("TAG", "fetchCurrentUserLocations: $locations")
        }
    }

    fun setWorkManagerStatus(status: Boolean) {
        _isWorkManageIsActive.value = status
    }
}