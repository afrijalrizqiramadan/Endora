package com.asyabab.endora.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.asyabab.endora.R
import com.asyabab.endora.base.BaseActivity
import com.asyabab.endora.data.models.checkout.CheckoutResponse
import com.asyabab.endora.data.models.payment.getpembelian.Unpaid
import com.asyabab.endora.utils.launchActivityWithNewTask
import com.asyabab.endora.utils.onClick
import kotlinx.android.synthetic.main.activity_pembayaranfinal.*
import java.text.DecimalFormat

class FinalPembayaranActivity : BaseActivity() {
    var checkout = CheckoutResponse()
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null
    var item = Unpaid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaranfinal)

        btBeranda.onClick {
            launchActivityWithNewTask<BerandaActivity>()
        }
        if (intent.getSerializableExtra("data") != null) {
            item = intent.getSerializableExtra("data") as Unpaid
            tvExpired.text = item.expireDate
            tv_va.text = item.invoiceNumber
            tv_amount.text = item.total?.convertRupiah()
            var namapembayaran = ""
            if (item.type.toString() == "xendit-bni") {
                namapembayaran = "BNI Virtual Account"
            } else if (item.type.toString() == "xendit-bri") {
                namapembayaran = "BRI Virtual Account"

            } else if (item.type.toString() == "xendit-mandiri") {
                namapembayaran = "Mandiri Virtual Account"
            } else if (item.type.toString() == "bca-va") {
                namapembayaran = "BCA Virtual Account"
            }
            tv_bank_name.text = namapembayaran
            tv_idpembelian.text = item.id
        } else {


            checkout = repository?.getCheckoutResponse()!!

            tvExpired.text = checkout.data?.transaction?.expiryDate
            tv_va.text = checkout.data?.transaction?.availableBanks?.get(0)?.bankAccountNumber
            tv_amount.text =
                checkout.data?.transaction?.availableBanks?.get(0)?.transferAmount?.convertRupiah()
            tv_bank_name.text =
                checkout.data?.transaction?.availableBanks?.get(0)?.bankCode + " " + checkout.data?.transaction?.availableBanks?.get(
                    0
                )?.bankBranch
            tv_idpembelian.text = checkout.data?.id
        }
        btSalin.onClick {
            myClipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val getstring: String? =
                checkout.data?.transaction?.availableBanks?.get(0)?.bankAccountNumber

            myClip = ClipData.newPlainText("text", getstring)
            myClipboard!!.setPrimaryClip(myClip!!)

            Toast.makeText(this@FinalPembayaranActivity, "Berhasil", Toast.LENGTH_SHORT).show();

        }
    }


    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    override fun onBackPressed() {

    }
}