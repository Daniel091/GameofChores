package sbd.pemgami


import android.content.Context
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
    private val TAG = "FindWGFragment"

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

        linLayout.visibility = View.GONE
        continueButton.visibility = View.GONE

        input_usrname.setText(CurrentUser.name)
        btnFind.setOnClickListener {
            hideKeyboard()
            findWG(input_wg_id.text.toString())
        }
        link_createWG.setOnClickListener { mListener?.triggerCardFlip() }
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
                    val wg = dataSnapshot.getValue(WG::class.java)
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
                notifyUser(wg)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
            }
        }
        getUser.addListenerForSingleValueEvent(getListener)
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
}
