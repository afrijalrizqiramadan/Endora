package com.asyabab.endora.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.ui.activity.cart.TasBelanjaActivity
import com.asyabab.endora.ui.fragment.*
import com.asyabab.endora.utils.launchActivity
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_pencarian.*


class PencarianActivity : BaseActivity() {
    private val mFocusFragment = FocusFragment()
    private val mHasilGagalFragment = HasilGagalFragment()
    private val mHasilPencarianFragment = HasilPencarianFragment()
    var buttonstate = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencarian)
        val viewPager: ViewPager = findViewById(R.id.vpager)
        tabs.setupWithViewPager(viewPager)
        tvIconNumber?.text = repository!!.getData("jumlahkeranjang")
        val adapter = TabAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)
        buttonstate = false

        btSearch.onClick {
            if(inputSearch.visibility == View.VISIBLE && inputSearch.hasFocus()){
                repository?.getToken()?.let {
                    inputSearch.clearFocus()
                    val imm =
                        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(
                        InputMethodManager.HIDE_IMPLICIT_ONLY,
                        0
                    )
                    loadFragment(mHasilPencarianFragment, inputSearch.text.toString())
                }
            }
            else{
                loadFragment(mFocusFragment)
                buttonstate = true
                frame_container.visibility = View.VISIBLE
                vpager.visibility = View.GONE
                tabs.visibility = View.GONE
                titleltoolbar.visibility = View.GONE
                inputSearch.visibility = View.VISIBLE

                inputSearch.requestFocus()
                inputSearch.postDelayed(Runnable {
                    val keyboard: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.showSoftInput(inputSearch, 0)
                }
                    , 200)


                inputSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                        inputSearch.clearFocus()
                        val imm =
                            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.toggleSoftInput(
                            InputMethodManager.HIDE_IMPLICIT_ONLY,
                            0
                        )
                        if (inputSearch.text.toString() == "") {
                            Toast.makeText(
                                this@PencarianActivity,
                                "Pencarian Kosong",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            loadFragment(mHasilPencarianFragment, inputSearch.text.toString())

                        }

                        true
                    } else false
                })
            }


        }

        handleIntent(this.intent)

        btBack.onClick {
            finish()
        }

        btTasBelanja.onClick {
            launchActivity<TasBelanjaActivity> { }
        }
    }

    private fun handleIntent(intent: Intent?) {
        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data
        showDeepLinkOffer(appLinkAction, appLinkData)
    }

    private fun showDeepLinkOffer(appLinkAction: String?, appLinkData: Uri?) {
        // 1
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            // 2
            val promotionCode = appLinkData.getQueryParameter("code")
            if (promotionCode.isNullOrBlank().not()) {
                Toast.makeText(this, "Gagal zz", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Gagal Memuat" , Toast.LENGTH_SHORT).show()

            }
        }
    }
    override fun onBackPressed() {
        if (buttonstate == false) {
            finish()
        } else {
            inputSearch.clearFocus()
            frame_container.visibility = View.GONE
            vpager.visibility = View.VISIBLE
            titleltoolbar.visibility = View.VISIBLE
            inputSearch.setText("")
            inputSearch.visibility = View.GONE
            adapter()
            buttonstate = false
        }
    }

    class TabAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
        private val tabName: Array<String> = arrayOf("Brand", "Jelajah")

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> BrandFragment()
            1 -> KategoriFragment()
            else -> FocusFragment()
        }

        override fun getCount(): Int = 2
        override fun getPageTitle(position: Int): CharSequence? = tabName.get(position)
    }


    abstract class TextChangedListener<T>(private val target: T) : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(s: Editable) {
            this.onTextChanged(target, s)
        }

        abstract fun onTextChanged(target: T, s: Editable?)

    }

    fun adapter() {
        tabs.visibility = View.VISIBLE
        val viewPager: ViewPager = findViewById(R.id.vpager)
        tabs.setupWithViewPager(viewPager)

        val adapter = TabAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)
    }

}