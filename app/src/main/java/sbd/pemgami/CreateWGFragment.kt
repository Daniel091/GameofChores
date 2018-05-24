package sbd.pemgami


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_create_wg.*

class CreateWGFragment : Fragment() {
    private val TAG = "CreateWGFragment"

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
                progressBar3.visibility = View.VISIBLE
                createWG(input_wgname.text.toString())
            }
        }
    }

    private fun createWG(wgname: String) {
        val wgReference = Constants.databaseWGs.push()
        val wg = WG(wgname, wgReference.key, CurrentUser.uid, listOf(CurrentUser.uid))

        wgReference.setValue(wg).addOnSuccessListener {
            CurrentWG.init(wgname, wgReference.key, CurrentUser.uid, listOf(CurrentUser.uid))
            Log.d(TAG, "WG Upload Successful")
            linkWGtoUser(wgReference.key)
        }
    }

    private fun linkWGtoUser(wg_id: String) {
        Constants.getCurrentUserWGRef()?.setValue(wg_id)?.addOnSuccessListener {
            CurrentUser.wg_id = wg_id
            Log.d(TAG, "User Update Successful")

            notifyUser()
        }
    }

    private fun notifyUser() {
        progressBar3.visibility = View.INVISIBLE

        val fadInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimation.duration = 2500

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
        startActivity(intent)
    }

}
