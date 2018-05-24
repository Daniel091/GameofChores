package sbd.pemgami


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            Log.d(TAG, "WG Upload Successful")
            linkWGtoUser(wgReference.key)
        }
    }

    private fun linkWGtoUser(wg_id: String) {
        Constants.getCurrentUserWGRef()?.setValue(wg_id)?.addOnSuccessListener {
            CurrentUser.wg_id = wg_id
            Log.d(TAG, "User Update Successful")
            moveToNextActivity()
        }
    }

    private fun moveToNextActivity() {
        progressBar3.visibility = View.INVISIBLE
        Log.d(TAG, "Move to next activity triggered")

        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
    }
}
