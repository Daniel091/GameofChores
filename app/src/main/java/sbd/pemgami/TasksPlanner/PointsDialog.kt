package sbd.pemgami.TasksPlanner

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sbd.pemgami.R
import kotlinx.android.synthetic.main.points_dialog.*


class PointsDialog : DialogFragment() {
    var points = 0

    companion object {
        fun newInstance(): PointsDialog {
            return PointsDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.points_dialog, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        points_earned.text = resources.getString(R.string.earned_points, points.toString())

        lottieGift.setOnClickListener {
            lottieGift.playAnimation()
        }
    }

}