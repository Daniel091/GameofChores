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
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_find_wg.*

class FindWGFragment : Fragment() {
    var mListener: CardButtonListener? = null
    private val TAG = "WGFragment"
    private var currentUser: User? = null
    private var foundWG: WG? = null

    // factory pattern
    companion object {
        fun newInstance(): FindWGFragment {
            return FindWGFragment()
        }
    }

    interface CardButtonListener {
        fun triggerCardFlip()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_find_wg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)

        linLayout.visibility = View.GONE
        continueButton.visibility = View.GONE

        input_usrname.setText(currentUser?.name)
        btnFind.setOnClickListener {
            hideKeyboard()
            findWG(input_wg_id.text.toString())
        }
        link_createWG.setOnClickListener { mListener?.triggerCardFlip() }
        continueButton.setOnClickListener {
            val new_name = input_usrname.text.toString()
            if (new_name != "") {
                val usr = currentUser?.copy(name = new_name)
                logUserInWG(usr, foundWG)
            }
        }
    }

    /*
        1. links user to WG -> Firebase and tries to update his name
        2. writes username to WG users list -> Firebase
        3. updates Shared Preferences LastUser and LastWG
        4. moves to HomeActivity
     */
    private fun logUserInWG(usr: User?, wg: WG?) {
        if (wg == null || usr == null) return
        progressBar2.visibility = View.VISIBLE

        val wgUsers = wg.users
        wgUsers.add(usr.uid)
        val updatedWG = wg.copy(users = wgUsers)

        val wgUsrRef = Constants.getCurrentUserWGRef(usr.uid)
        wgUsrRef?.setValue(wg.uid)?.addOnSuccessListener {
            Log.d(TAG, "Linked User Successful")

            val wgUsrName = Constants.getUserNameRef(usr.uid)
            wgUsrName?.setValue(usr.name)

            val wgRef = Constants.databaseWGs.child(wg.uid)
            wgRef.setValue(updatedWG).addOnSuccessListener {
                updateSharedPreferences(usr, updatedWG)
                moveToHomeActivity()
            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as CardButtonListener
    }

    private fun findWG(id: String) {
        progressBar2.visibility = View.VISIBLE

        val getWG: Query = Constants.databaseWGs.orderByChild("uid").equalTo(id)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount > 0) {
                    Log.d(TAG, "WG gefunden :-)")
                    fetchWGData(id)
                } else {
                    Toast.makeText(context, getString(R.string.no_wg), Toast.LENGTH_LONG).show()
                    progressBar2.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
                progressBar2.visibility = View.INVISIBLE
            }
        }
        getWG.addListenerForSingleValueEvent(getListener)
    }


    private fun fetchWGData(wg_id: String) {
        val getUser: Query = Constants.databaseWGs.child(wg_id)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wg = dataSnapshot.getValue(WG::class.java)
                Log.d(TAG, "Got WG: ${wg?.name}")
                if (wg == null) return
                foundWG = wg
                notifyUser(wg)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
            }
        }
        getUser.addListenerForSingleValueEvent(getListener)
    }

    private fun updateSharedPreferences(usr: User?, wg: WG) {
        val linkedUsr = usr?.copy(wg_id = wg.uid)
        if (linkedUsr != null) {
            SharedPrefsUtils.writeUserToSharedPref(activity?.applicationContext, linkedUsr)
            SharedPrefsUtils.writeWGToSharedPref(activity?.applicationContext, wg)
        }
    }


    private fun notifyUser(wg: WG?) {
        progressBar2.visibility = View.INVISIBLE
        textView2.text = resources.getString(R.string.found_wg, wg?.name, input_usrname.text)

        val fadInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadInAnimation.duration = 3000

        linLayout.startAnimation(fadInAnimation)
        continueButton.startAnimation(fadInAnimation)
        linLayout.visibility = View.VISIBLE
        continueButton.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(content.windowToken, 0)
    }

    private fun moveToHomeActivity() {
        progressBar2.visibility = View.INVISIBLE
        Log.d(TAG, "Move to next activity triggered")

        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
