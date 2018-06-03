package sbd.pemgami

import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(applicationContext)

        val headerView = nav_view.getHeaderView(0)
        headerView.wg_title.text = wg?.name
        headerView.usr_name.text = usr?.name

        val tag = HomeFragment::class.java.simpleName
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.mainFrame, HomeFragment.newInstance())
        trans.addToBackStack(tag)
        trans.commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            // Last Fragment is always HomeFragment
            if (supportFragmentManager.backStackEntryCount == 1) {
                supportFragmentManager.popBackStack()
            }
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var tag: String? = null
        when (item.itemId) {
            R.id.nav_home -> {
                fragment = HomeFragment()
                tag = "HomeFragment"
            }
            R.id.nav_tasks -> {
                showToast()
            }
            R.id.nav_rankings -> {
                showToast()
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment()
                tag = "HomeFragment"
            }
        }

        if (fragment == null) return true
        setUpFragment(fragment)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun setUpFragment(fragment: Fragment) {
        val tag = fragment.javaClass.simpleName
        val manager = supportFragmentManager
        val fragInStack = isFragmentInBackStack(manager, tag)

        if (fragInStack) {
            manager.popBackStackImmediate(tag, 0)
        } else {
            val trans = manager.beginTransaction()
            trans.replace(R.id.mainFrame, fragment)
            trans.addToBackStack(tag)
            trans.commit()
        }
    }

    private fun isFragmentInBackStack(fragmentManager: FragmentManager, fragmentTagName: String): Boolean {
        for (entry in 0 until fragmentManager.backStackEntryCount) {
            if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).name) {
                return true
            }
        }
        return false
    }

    private fun showToast(text: String = "Not implemented") {
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
        toast.show()
    }

}
