package sbd.pemgami

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson

object SharedPrefsUtils {
    private val TAG = "SharedPrefsUtils"

    fun writeUserToSharedPref(context: Context, user: User) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()

        val json = gson.toJson(user)
        prefsEditor.putString("LastUser", json)
        prefsEditor.commit()
        Log.d(TAG, "Wrote LastUser")
    }

    fun writeUserToSharedPref(context: Context?, user: User): Boolean {
        if (context == null) return false
        writeUserToSharedPref(context, user)
        return true
    }

    fun readLastUserFromSharedPref(context: Context?): User? {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()

        val json = mPrefs.getString("LastUser", "")
        if (json != "") {
            val usr = gson.fromJson(json, User::class.java)
            if (usr != null) {
                return usr
            }
        }
        Log.d(TAG, "No LastUser could be read")
        return null
    }

    fun _debugClearPreferences(context: Context?) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = mPrefs.edit()
        prefsEditor.clear()
        prefsEditor.commit()
    }
}