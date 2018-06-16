package sbd.pemgami.MonthlyWinner

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import sbd.pemgami.Constants
import sbd.pemgami.SharedPrefsUtils
import sbd.pemgami.User
import sbd.pemgami.WG
import java.util.*

class MonthlyWinnerHandler(val handler: MonthlyWinnerCallback, val context: Context, val wg: WG) {


    interface MonthlyWinnerCallback {
        fun triggerAnimationFragment(winner: User)
    }

    fun checkNeedsPastTaskClear() {
        // today
        val calendar = Calendar.getInstance()

        // past
        val calSharedPref = Calendar.getInstance()
        calSharedPref.time = Date(wg.time_cleared)

        // if today is after past && (today has different month than past date, or if years are different)
        if (checkCal1IsAfterCal2ByMonth(calendar, calSharedPref)) {
            doubleCheckFetchWG(wg, calendar)
        }
    }


    // double check, if someone else cleared tasks already, so fetch wg data again
    private fun doubleCheckFetchWG(wg: WG?, calNow: Calendar) {
        val query = Constants.databaseWGs.child(wg?.uid)
        val mListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val nWg = snapshot?.getValue(WG::class.java)
                nWg?.let {
                    SharedPrefsUtils.writeWGToSharedPref(context, nWg)
                    val cDate = Date(nWg.time_cleared)
                    val calThen = Calendar.getInstance()
                    calThen.time = cDate

                    // if true user is the one who clears past_tasks, and usr points
                    if (checkCal1IsAfterCal2ByMonth(calNow, calThen)) {
                        clearPastData(nWg, calNow.time.time)
                        // computes Winner, resets Users, triggers AnimationFragment
                        computerWinner(nWg)
                    } else {
                        // if false, usr had outdated wg object, but task where already cleared, and winner was calculated
                        getWinner(nWg)
                    }
                }
            }
        }
        query.addListenerForSingleValueEvent(mListener)
    }

    private fun getWinner(nWg: WG) {
        val query = Constants.databaseWinners.child(wg.uid)
        val mListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val winner = snapshot?.getValue(User::class.java)
                winner?.let {
                    handler.triggerAnimationFragment(winner)
                }
            }

        }
        query.addListenerForSingleValueEvent(mListener)
    }

    private fun computerWinner(nWg: WG) {
        val query = Constants.databaseUsers.orderByChild("wg_id")?.equalTo(nWg.uid)
        val mListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot == null) return
                val users = mutableListOf<User>()
                for (child in snapshot.children) {
                    val map = child.value as HashMap<*, *>
                    val points = map["points"] as Long
                    val usr = User(map["name"] as String, map["uid"] as String, map["email"] as String, map["wg_id"] as String, points.toInt())
                    users.add(usr)
                }
                users.sortByDescending { it.points }
                if (users.count() > 0) {
                    val winner = users.first()
                    setWinner(nWg, winner)
                    resetUsersPoints(nWg)
                    handler.triggerAnimationFragment(winner)
                }
            }
        }
        query?.addListenerForSingleValueEvent(mListener)
    }


    private fun setWinner(wg: WG, winner: User) {
        Constants.databaseWinners.child(wg.uid).setValue(winner)
    }

    private fun clearPastData(nWg: WG, time: Long) {
        // clear past tasks of wg
        Constants.databasePastTasks.child(nWg.uid).removeValue()

        // update cleared_time
        Constants.databaseWGs.child(nWg.uid).child("time_cleared").setValue(time)

        // update shared pref wg
        val updatedWg = nWg.copy(time_cleared = time)
        SharedPrefsUtils.writeWGToSharedPref(context, updatedWg)
    }


    private fun resetUsersPoints(nWg: WG) {
        val cUsr = SharedPrefsUtils.readLastUserFromSharedPref(context)
        for (uid in nWg.users) {
            Constants.databaseUsers.child(uid)?.child("points")?.setValue(0)

            if (uid == cUsr?.uid) {
                val updatedUsr = cUsr.copy(points = 0)
                SharedPrefsUtils.writeUserToSharedPref(context, updatedUsr)
            }
        }
    }

    private fun checkCal1IsAfterCal2ByMonth(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.after(cal2) && (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) || cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
    }
}