package sbd.pemgami


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.edit_text_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import sbd.pemgami.LoginFlow.StartActivity


class SettingsFragment : Fragment() {
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onStart() {
        super.onStart()
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        userName.text = "${usr?.name}"
        activity?.title = "Settings"

        //debug button
        logoutBtn.setOnClickListener { logUsrOut() }

        // debug button
        clearShPrefBtn.setOnClickListener {
            // cleans your shared preferences usr and wg data
            SharedPrefsUtils._debugClearPreferences(activity?.applicationContext)
            logUsrOut()
        }

        //Change User Name on Click
        changeUsrButton.setOnClickListener {
            context?.let {
                val editAlert = AlertDialog.Builder(context).create()
                val editView = layoutInflater.inflate(R.layout.edit_text_layout, null)
                editAlert.setView(editView)
                editAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                    updateUsername(editAlert.alert_dialog_edittext.text.toString())

                }
                editAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                }

                editAlert.show()
            }
        }

        //delete button
        // TODO: Add Dialog "Are you sure..."
        leaveWG.setOnClickListener {
            removeUser()
        }
    }

    private fun updateUsername(newUsername: String) {
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)

        Toast.makeText(context, "Your new username is:\n$newUsername", Toast.LENGTH_LONG).show()
        userName.text = newUsername

        Constants.databaseUsers.child(usr?.uid).child("name").setValue(newUsername)
        usr?.let {
            val uUser = usr.copy(name = newUsername)
            SharedPrefsUtils.writeUserToSharedPref(activity?.applicationContext, uUser)
        }
    }


    private fun logUsrOut() {
        if (fbAuth.currentUser != null) {
            Log.d(HomeFragment.getTagName(), "Signed User Out")

            val context = activity?.applicationContext
            context?.let {
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener {
                            val intent = Intent(context, StartActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
            }
        }
    }

    private fun removeUser() {
        val context = activity?.applicationContext
        // remove user
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(context)
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(context)
        val uUser = usr?.copy(wg_id = "")
        wg?.users?.remove(usr?.uid)


        // remove user wg_id attribute
        Constants.databaseUsers.child(usr?.uid).setValue(uUser).addOnSuccessListener {
            uUser?.let { SharedPrefsUtils.writeUserToSharedPref(context, uUser) }
            SharedPrefsUtils.removeLastWG(context)

            // save wg obj without user_uid
            val wgUsrRef = Constants.databaseWGs.child(wg?.uid)
            wgUsrRef?.setValue(wg)?.addOnSuccessListener {

                // navigate to StartActivity
                val intent = Intent(context, StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}
