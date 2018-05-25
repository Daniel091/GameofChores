package sbd.pemgami

import android.content.Context
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_create_wg.*


// Fragment extension
fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(content.windowToken, 0)
}