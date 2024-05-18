package com.asyabab.endora.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseFragment
import com.asyabab.endora.data.models.view.SlideshowViewModel
import com.asyabab.endora.ui.activity.*
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_jelajah.*
import kotlinx.android.synthetic.main.fragment_profilsaya.view.*

class ProfilSayaFragment : BaseFragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var rootRef: DatabaseReference? = null

    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profilsaya, container, false)
        rootRef = FirebaseDatabase.getInstance().reference
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        activity?.titletoolbar?.text = "Profil Saya" // Key, default value

        root.btRincianProfil.onClick {
            val intent = Intent(this@ProfilSayaFragment.context, RincianProfilActivity::class.java)
            startActivity(intent)
        }
//        root.btPemberitahuan.onClick {
//            val intent = Intent(this@ProfilSayaFragment.context, PemberitahuanActivity::class.java)
//            startActivity(intent)
//
//        }
        root.btPengaturanAlamat.onClick {
            val intent =
                Intent(this@ProfilSayaFragment.context, AturAlamatActivity::class.java)
            startActivity(intent)
        }
        root.btPengaturanLain.onClick {
            val intent = Intent(this@ProfilSayaFragment.context, PengaturanLainActivity::class.java)
            startActivity(intent)
        }
        root.btLogout.onClick {
            signOut()
        }
        return root
    }

    fun signOut() {
        repository!!.saveToken("")
        repository!!.setFirstTimeLaunch(true)
        mAuth!!.signOut()
        mLoginManager?.logOut()
        mGoogleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                requireActivity().launchActivityWithNewTask<LoginActivity>()
            }
        }
    }
}