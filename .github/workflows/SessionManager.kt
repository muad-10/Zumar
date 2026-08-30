package com.zumar.app.util

import android.content.Context
import android.content.SharedPreferences
import com.zumar.app.model.User
import org.json.JSONArray
import org.json.JSONObject

/**
 * Very simple on-device "backend": stores registered users and the
 * logged-in session inside SharedPreferences as JSON. This is a demo
 * account system only — there is no server and no real money involved.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("zumar_prefs", Context.MODE_PRIVATE)

    fun registerUser(user: User): Boolean {
        val users = getAllUsersJson()
        for (i in 0 until users.length()) {
            val obj = users.getJSONObject(i)
            if (obj.getString("email").equals(user.email, ignoreCase = true)) {
                return false // email already registered
            }
        }
        users.put(userToJson(user))
        prefs.edit().putString(KEY_USERS, users.toString()).apply()
        return true
    }

    fun login(email: String, password: String): User? {
        val users = getAllUsersJson()
        for (i in 0 until users.length()) {
            val obj = users.getJSONObject(i)
            if (obj.getString("email").equals(email, ignoreCase = true) &&
                obj.getString("password") == password
            ) {
                setLoggedInEmail(email)
                return jsonToUser(obj)
            }
        }
        return null
    }

    fun getCurrentUser(): User? {
        val email = prefs.getString(KEY_CURRENT_EMAIL, null) ?: return null
        val users = getAllUsersJson()
        for (i in 0 until users.length()) {
            val obj = users.getJSONObject(i)
            if (obj.getString("email").equals(email, ignoreCase = true)) {
                return jsonToUser(obj)
            }
        }
        return null
    }

    fun updateBalance(newBalance: Double) {
        val email = prefs.getString(KEY_CURRENT_EMAIL, null) ?: return
        val users = getAllUsersJson()
        for (i in 0 until users.length()) {
            val obj = users.getJSONObject(i)
            if (obj.getString("email").equals(email, ignoreCase = true)) {
                obj.put("balance", newBalance)
            }
        }
        prefs.edit().putString(KEY_USERS, users.toString()).apply()
    }

    fun logout() {
        prefs.edit().remove(KEY_CURRENT_EMAIL).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getString(KEY_CURRENT_EMAIL, null) != null

    private fun setLoggedInEmail(email: String) {
        prefs.edit().putString(KEY_CURRENT_EMAIL, email).apply()
    }

    private fun getAllUsersJson(): JSONArray {
        val raw = prefs.getString(KEY_USERS, "[]")
        return JSONArray(raw)
    }

    private fun userToJson(u: User): JSONObject {
        val obj = JSONObject()
        obj.put("firstName", u.firstName)
        obj.put("middleName", u.middleName ?: "")
        obj.put("lastName", u.lastName)
        obj.put("phone", u.phone)
        obj.put("email", u.email)
        obj.put("password", u.password)
        obj.put("address", u.address)
        obj.put("state", u.state)
        obj.put("dob", u.dob)
        obj.put("gender", u.gender)
        obj.put("pin", u.pin)
        obj.put("balance", u.balance)
        return obj
    }

    private fun jsonToUser(obj: JSONObject): User {
        return User(
            firstName = obj.getString("firstName"),
            middleName = obj.optString("middleName", ""),
            lastName = obj.getString("lastName"),
            phone = obj.getString("phone"),
            email = obj.getString("email"),
            password = obj.getString("password"),
            address = obj.getString("address"),
            state = obj.getString("state"),
            dob = obj.getString("dob"),
            gender = obj.getString("gender"),
            pin = obj.getString("pin"),
            balance = obj.optDouble("balance", 0.0)
        )
    }

    companion object {
        private const val KEY_USERS = "users"
        private const val KEY_CURRENT_EMAIL = "current_email"
    }
}
