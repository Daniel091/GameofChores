package sbd.pemgami


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*
import sbd.pemgami.LoginFlow.StartActivity
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_home.*


class SettingsFragment : Fragment() {
    private val fbAuth = FirebaseAuth.getInstance()
    public var selectedColor = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onStart() {
        super.onStart()

        activity?.title = "Settings"

        //debug button
        logoutBtn.setOnClickListener { logUsrOut() }

        // debug button
        clearShPrefBtn.setOnClickListener {
            // cleans your shared preferences usr and wg data
            SharedPrefsUtils._debugClearPreferences(activity?.applicationContext)
            logUsrOut()
        }

        ThemeRadioGroup.setOnCheckedChangeListener({ group, checkedId ->
            val radioButton = view?.findViewById<RadioButton>(checkedId)
            selectedColor = radioButton?.text.toString()
            Toast.makeText(activity, radioButton?.text.toString() + " is selected", Toast.LENGTH_LONG).show()
            if (id!=-1){
                //Toast.makeText(activity,  " Debug!", Toast.LENGTH_LONG).show()
                if (selectedColor == "Grey Theme" ){
                    // TODO: Set Toolbar Color somehow....
                    //toolbar.setBackgroundResource(R.color.colorWhite)
                } else if (selectedColor == "Orange Theme" ){
                    // set Toolbar Color to Orange somehow...
                }
            }
        })
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
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
}

