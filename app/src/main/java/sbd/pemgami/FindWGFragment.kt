package sbd.pemgami


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        input_usrname.setText(CurrentUser.name)
        btnFind.setOnClickListener { findWG(input_wg_id.text.toString()) }
        link_createWG.setOnClickListener { mListener?.triggerCardFlip() }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as CardButtonListener
    }

    private fun findWG(id: String) {
        progressBar2.visibility = View.VISIBLE

        val getWG: Query = Constants.databaseWGs.child("id").equalTo(id)

        val getListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val usr = dataSnapshot.getValue(User::class.java)
                //Log.d(TAG, "Got User: ${usr?.email}")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "ErrorCode ${databaseError.code}, DatabaseDetails: ${databaseError.details}")
                //progressBar2.visibility = View.INVISIBLE
            }
        }
        getWG.addListenerForSingleValueEvent(getListener)
    }

}
