package com.example.gttest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gttest.gtenums.FailureReasaonEnum
import com.example.gttest.utils.CONTACT_NAME
import com.example.gttest.utils.CONTACT_NUMBER
import com.example.gttest.view.ContactsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    var contactName: String = ""
    var contactNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    companion object {

        val USD: Float = 7.8F

    }


    private fun initUI() {

        btn_fetch_contact.setOnClickListener {

            lbl_error.visibility = View.GONE
            val intent = Intent(this@MainActivity, ContactsActivity::class.java)
            startActivityForResult(intent, 101)
        }
        edtamount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isNullOrEmpty()) {
                    //dataViewModel.isValidaData(s.toString())
                    isValidData(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lbl_error.visibility = View.GONE
            }

        })


        btn_submit.setOnClickListener {

            if (contactNumber.isNotEmpty() && contactName.isNotEmpty()&& edtamount.toString().isNotBlank()) {

                Log.d("Valid case","")
            } else {

                lbl_error.visibility = View.VISIBLE
                lbl_error.setTextColor(getResources().getColor(R.color.red))
                lbl_error.text = "Please select the contact from contact book first"

            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun isValidData(enteredData: String) {

        var message: String = ""
        var value = enteredData.toBigDecimal()
        var valueinUSD: BigDecimal = calculateHKDtoUSD(value)
        lbl_error.visibility = View.VISIBLE
        if (valueinUSD > 10000.toBigDecimal()) {
            lbl_error.setTextColor(getResources().getColor(R.color.red))
            lbl_error.text = FailureReasaonEnum.GT_GREATER.value
        } else if (value <= 0.toBigDecimal()) {
            lbl_error.setTextColor(getResources().getColor(R.color.red))
            lbl_error.text = FailureReasaonEnum.GT_LESSTHANZERO.value
        } else if (valueinUSD < 10000.toBigDecimal() && value > 0.toBigDecimal()) {
            lbl_error.setTextColor(getResources().getColor(R.color.black))
            lbl_error.text =
                "Send USD $valueinUSD to $contactName with phone number: $contactNumber"
        }


    }

    private fun calculateHKDtoUSD(value: BigDecimal): BigDecimal {

        return value / USD.toBigDecimal()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 101 && data != null) {
            contactName = data!!.getStringExtra(CONTACT_NAME)
            contactNumber = data!!.getStringExtra(CONTACT_NUMBER)
            lbl_send_to.setText(resources.getString(R.string.lbl_send_to) + contactName)
            lbl_contact.setText(resources.getString(R.string.lbl_phone) + contactNumber)

        }
    }

}