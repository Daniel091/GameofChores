package sbd.pemgami

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wgform.*


class WGFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wgform)

        input_usrname.setText(CurrentUser.name)
        btnFind.setOnClickListener { findWG(input_wg_id.text.toString())  }
    }

    private fun findWG(id: String) {

    }
}

