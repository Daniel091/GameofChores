package sbd.pemgami


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        activity?.title = "Home"
        val usr = SharedPrefsUtils.readLastUserFromSharedPref(activity?.applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(activity?.applicationContext)
        debugLabel.text = "Username: ${usr?.name}, WG: ${wg?.name}"

        val img = BitmapFactory.decodeResource(resources, R.drawable.welcome_image)
        var round = RoundedBitmapDrawableFactory.create(resources, img)
        round.isCircular = true
        welcomeImageView.setImageDrawable(round)


    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

        fun getTagName(): String = "HomeFragment"
    }
}
