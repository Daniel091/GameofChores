package sbd.pemgami

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson

object SharedPrefsUtils {
    private val TAG = "SharedPrefsUtils"

    fun removeLastWG(context: Context?) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val prefsEditor = mPrefs.edit()

        val emptyWG = WG()
        val json = gson.toJson(emptyWG)
        prefsEditor.putString("LastWG", json)
        prefsEditor.commit()
    }

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
    fun writeWGToSharedPrefJava(context: Context, wg: WG) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()

        val json = gson.toJson(wg)
        prefsEditor.putString("LastWG", json)
        prefsEditor.commit()
        Log.d(TAG, "Wrote LastWG")
    }

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
    fun writeUserToSharedPrefJava(context: Context, user: User) {
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