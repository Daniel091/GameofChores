package sbd.pemgami


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)
        debugLabel.text = "Username: ${usr?.name}, WG: ${wg?.name}"

        //debug button
        logoutBtn.setOnClickListener { logUsrOut() }

        // debug button
        clearShPrefBtn.setOnClickListener {
            // cleans your shared preferences usr and wg data
            SharedPrefsUtils._debugClearPreferences(activity?.applicationContext)
            logUsrOut()
        }
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

        fun getTagName(): String = "HomeFragment"
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
