package com.example.news_backend.data.sharedpreferences

import android.content.Context

class DataLocalManager private constructor(context: Context) {
    private val FIRST_SHARED_PREFERENCES = "FIRST_SHARED_PREFERENCES"
    private val mySharedPreferences: MySharedPreferences = MySharedPreferences(context)

    companion object {
        private lateinit var instance: DataLocalManager

        fun init(mContext: Context) {
            if (!::instance.isInitialized) {
                instance = DataLocalManager(mContext)
            }
        }

        fun getInstance(): DataLocalManager {
            if (!::instance.isInitialized) {
                throw UninitializedPropertyAccessException("DataLocalManager chưa được khởi tạo.")
            }
            return instance
        }
    }

    fun setFirstInstalled(isFirst: Boolean) {
        mySharedPreferences.putBooleanValue(FIRST_SHARED_PREFERENCES, isFirst)
    }

    fun getFirstInstalled(): Boolean {
        return mySharedPreferences.getBooleanValue(FIRST_SHARED_PREFERENCES)
    }


    fun setSaveUserInfo(id: Long, name: String, username: String, email: String, role: String) {
        mySharedPreferences.saveUserInfo(id, name, username, email, role)
    }
    fun removeValueFromSharedPreferences() {
        mySharedPreferences.removeValueFromSharedPreferences();
    }
    fun setSaveTokenKey(token: String) {
        mySharedPreferences.saveTokenKey(token)
    }

    fun getInfoUserId(): Long {
        return mySharedPreferences.getUserId()
    }
    fun getInfoName(): String? {
        return mySharedPreferences.getName()
    }
    fun getInfoUserName(): String? {
        return mySharedPreferences.getUserName()
    }
    fun getInfoEmail(): String? {
        return mySharedPreferences.getEmail()
    }
    fun getInfoUserRole(): String? {
        return mySharedPreferences.getUserRole()
    }
    fun getInfoTokenKey(): String? {
        return mySharedPreferences.getTokenKey()
    }


}