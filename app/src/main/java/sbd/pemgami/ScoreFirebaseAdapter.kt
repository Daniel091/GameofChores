package sbd.pemgami

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.score_row_layout.view.*


class ScoreFirebaseAdapter(frag: ScoreRankingFragment, usr: User, wg: WG) : RecyclerView.Adapter<ScoreFirebaseAdapter.UserHolder>() {
    var mListener: BuildEventHandler? = frag
    private val mUsr = usr
    private val mWg = wg
    private val users = mutableListOf<User>()
    private val context = frag.context

    init {
        val query = Constants.databaseUsers.orderByChild("wg_id")?.equalTo(mWg.uid)

        val childListener = object : ChildEventListener {
            override fun onCancelled(snapshot: DatabaseError?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot?, preKey: String?) {
                // we do not plan to move childs
            }

            override fun onChildChanged(snapshot: DataSnapshot?, preKey: String?) {
                val index = users.indexOfFirst { it.uid == preKey }
                if (index != -1) {
                    val user = snapshot?.getValue(User::class.java)
                    user?.let {
                        users[index] = user
                        notifyItemChanged(index)
                    }
                }
            }

            // sorts added users by points
            override fun onChildAdded(snapshot: DataSnapshot?, preKey: String?) {
                val user = snapshot?.getValue(User::class.java)

                val index = if (users.count() != 0) users.count() - 1 else 0
                user?.let {
                    users.add(index, user)
                    users.sortByDescending { it.points }
                    notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot?) {
                val user = snapshot?.getValue(User::class.java)
                user?.let {
                    val index = users.indexOfFirst { it.uid == user.uid }
                    if (index != -1) {
                        users.remove(user)
                        notifyItemRemoved(index)
                    }
                }
            }

        }
        query?.addChildEventListener(childListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.score_row_layout, parent, false)

        // trigger data is there, because layout inflation happens
        mListener?.triggerBuildHappened()

        return UserHolder(view)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.setUser(users[position], position, context)
    }

    interface BuildEventHandler {
        fun triggerBuildHappened()
    }


    class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v


        init {
            v.setOnClickListener(this)
        }

        fun setUser(usr: User, position: Int, context: Context?) {
            val text = context?.resources?.getString(R.string.score_rank, (position + 1).toString(), usr.name, usr.points.toString())
            view.nameLine.text = text
        }

        override fun onClick(p0: View?) {

        }

        companion object {
            private val Task_KEY = "User"
        }
    }
}