package sbd.pemgami

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import android.widget.TextView
import android.widget.Toast


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val fbAuth = FirebaseAuth.getInstance()
    private val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // debug button
        logoutBtn.setOnClickListener { logUsrOut() }

        // debug button
        clearShPrefBtn.setOnClickListener {
            // cleans your shared preferences usr and wg data
            SharedPrefsUtils._debugClearPreferences(applicationContext)
            logUsrOut()
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()

        val usr = SharedPrefsUtils.readLastUserFromSharedPref(applicationContext)
        val wg = SharedPrefsUtils.readLastWGFromSharedPref(applicationContext)
        debugLabel.text = "Username: ${usr?.name}, WG: ${wg?.name}"

        val headerView = nav_view.getHeaderView(0)
        val navUsernameText = headerView.findViewById(R.id.usr_name) as TextView
        val navWGText = headerView.findViewById(R.id.wg_title) as TextView

        navWGText.text = wg?.name
        navUsernameText.text = usr?.name
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                showToast()
            }
            R.id.nav_tasks -> {
                showToast()
            }
            R.id.nav_rankings -> {
                showToast()
            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logUsrOut() {
        if (fbAuth.currentUser != null) {
            Log.d(TAG, "Signed User Out")
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        val intent = Intent(this, StartActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
        }
    }

    private fun showToast(text: String = "Not implemented") {
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
        toast.show()
    }

}
