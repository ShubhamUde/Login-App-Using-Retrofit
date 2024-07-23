package com.shubham.retrofitexample

import android.content.Context

class SharedPrefManager(private val context: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "shared_pref_name"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveUser(user: User) {
        editor.putInt("id", user.id)
        editor.putString("username", user.username)
        editor.putString("email", user.email)
        editor.putBoolean("logged", true)
        editor.apply()
    }

    fun isLogged(): Boolean {
        return sharedPreferences.getBoolean("logged", false)
    }

    fun getUser(): User? {
        val id = sharedPreferences.getInt("id", -1)
        val username = sharedPreferences.getString("username", null)
        val email = sharedPreferences.getString("email", null)

        return if (id != -1 && username != null && email != null) {
            User(id, username, email)
        } else {
            null
        }
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
