package sbd.pemgami

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson

object SharedPrefsUtils {
    private val TAG = "SharedPrefsUtils"

    @JvmStatic
    fun writeWGToSharedPref(context: Context, wg: WG) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()

        val json = gson.toJson(wg)
        prefsEditor.putString("LastWG", json)
        prefsEditor.commit()
        Log.d(TAG, "Wrote LastWG")
    }

    @JvmStatic
    fun writeWGToSharedPref(context: Context?, wg: WG): Boolean {
        if (context == null) return false
        writeWGToSharedPref(context, wg)
        return true
    }

    @JvmStatic
    fun readLastWGFromSharedPref(context: Context?): WG? {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()

        val json = mPrefs.getString("LastWG", "")
        if (json != "") {
            val wg = gson.fromJson(json, WG::class.java)
            if (wg != null) {
                return wg
            }
        }
        Log.d(TAG, "No LastWG could be read")
        return null
    }


    @JvmStatic
    fun writeUserToSharedPref(context: Context, user: User) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()

        val json = gson.toJson(user)
        prefsEditor.putString("LastUser", json)
        prefsEditor.commit()
        Log.d(TAG, "Wrote LastUser")
    }

    @JvmStatic
    fun writeUserToSharedPref(context: Context?, user: User): Boolean {
        if (context == null) return false
        writeUserToSharedPref(context, user)
        return true
    }

    @JvmStatic
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