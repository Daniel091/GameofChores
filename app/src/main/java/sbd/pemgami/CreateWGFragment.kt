package sbd.pemgami


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_create_wg.*

class CreateWGFragment : Fragment() {
    private val TAG = "CreateWGFragment"
    private var currentUser: User? = null

    companion object {
        fun newInstance(): CreateWGFragment {
            return CreateWGFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_wg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linLayout.visibility = View.GONE
        continueButton.visibility = View.GONE

        createWgBtn.setOnClickListener {
            val wgname = input_wgname.text.toString()
            if (wgname != "") {
                hideKeyboard()

                progressBar3.visibility = View.VISIBLE
                input_wgname.isEnabled = false
                createWG(input_wgname.text.toString())
            }
        }

        copyBtn.setOnClickListener {
            copyText(codeTextView.text)
        }

        val act = activity ?: return
        currentUser = SharedPrefsUtils.readLastUserFromSharedPref(act.applicationContext)
    }

    private fun copyText(text: CharSequence) {
        val clipM = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipM.primaryClip = clipData

        Toast.makeText(context, "WG ID Copied", Toast.LENGTH_LONG).show()
    }

    private fun createWG(wgname: String) {
        val usr = currentUser ?: return

        val wgReference = Constants.databaseWGs.push()
        val wg = WG(wgname, wgReference.key, usr.uid, listOf(usr.uid))

        wgReference.setValue(wg).addOnSuccessListener {
            CurrentWG.init(wgname, wgReference.key, usr.uid, listOf(usr.uid))
            Log.d(TAG, "WG Upload Successful")
            linkWGtoUser(wgReference.key, usr)
        }
    }

    private fun linkWGtoUser(wg_id: String, usr: User) {
        Constants.getCurrentUserWGRef(usr.uid)?.setValue(wg_id)?.addOnSuccessListener {
            val linkedUsr = usr.copy(wg_id = wg_id)
            Log.d(TAG, "User Update Successful")

            SharedPrefsUtils.writeUserToSharedPref(activity?.applicationContext, linkedUsr)
            notifyUser()
        }
    }

    private fun notifyUser() {
        progressBar3.visibility = View.INVISIBLE

        val fadInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimation.duration = 3000

        codeTextView.text = CurrentWG.uid
        textView2.text = resources.getString(R.string.wg_created, CurrentWG.name)
        linLayout.visibility = View.VISIBLE
        linLayout.startAnimation(fadInAnimation)

        continueButton.visibility = View.VISIBLE
        continueButton.startAnimation(fadInAnimation)

        continueButton.setOnClickListener {
            moveToNextActivity()
        }
    }

    private fun moveToNextActivity() {
        Log.d(TAG, "Move to next activity triggered")

        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(content.windowToken, 0)
    }
}
