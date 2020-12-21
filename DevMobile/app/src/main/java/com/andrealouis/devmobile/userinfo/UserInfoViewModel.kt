package com.andrealouis.devmobile.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel()  {

    private val repository = UserInfoRepository()
    private val _userInfo = MutableLiveData<UserInfo>()
    public val userInfo: LiveData<UserInfo> = _userInfo


    fun loadUserInfo() {
        viewModelScope.launch {
            val fetchedUserInfo = repository.refresh()
            if (fetchedUserInfo != null)
                _userInfo.value = fetchedUserInfo!!
        }
    }

    fun editTask(editUserInfo: UserInfo) {
        viewModelScope.launch {
            val editedUserInfo = repository.update(editUserInfo)
            if (editedUserInfo != null) {
                _userInfo.value = editedUserInfo!!
            }
        }
    }
}