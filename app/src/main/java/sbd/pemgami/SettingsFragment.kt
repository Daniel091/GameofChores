package sbd.pemgami


import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)

        userName.text = "${usr?.name}"
        activity?.title = "Settings"

        codeTextViewSettings.text = wg?.uid

        //debug button
        logoutBtn.setOnClickListener { logUsrOut() }

        //Change User Name on Click
        changeUsrButton.setOnClickListener {
            context?.let {
                val editAlert = AlertDialog.Builder(context).create()
                val editView = layoutInflater.inflate(R.layout.edit_text_layout, null)
                editAlert.setView(editView)
                editAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                    if (editAlert.alert_dialog_edittext.text.isNotEmpty()){
                        updateUsername(editAlert.alert_dialog_edittext.text.toString())
                    } else {
                        Toast.makeText(context, "Username remains unchanged", Toast.LENGTH_LONG).show()
                    }
                }
                editAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                }

                editAlert.show()
            }
        }

        //delete button
        leaveWG.setOnClickListener {
            val c = context
            c?.let {
                displayLeaveDialog(c, wg)
            }
        }

        copyBtnSettings.setOnClickListener {
            copyText(codeTextViewSettings.text)
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

    private fun displayLeaveDialog(context: Context, wg: WG?) {
        val builder = android.support.v7.app.AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.really_leave))
        builder.setMessage(context.resources.getString(R.string.leave_desc, wg?.name))

        // Leave WG
        builder.setPositiveButton("Ok") { _, _ ->
            Toast.makeText(context, "Jetzt austreten!", Toast.LENGTH_LONG).show()
            removeUser()
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // closes dialog
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun copyText(text: CharSequence) {
        val clipM = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipM.primaryClip = clipData

        Toast.makeText(context, "WG ID Copied", Toast.LENGTH_LONG).show()
    }
}
