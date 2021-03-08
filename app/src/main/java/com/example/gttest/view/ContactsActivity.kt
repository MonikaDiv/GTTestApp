package com.example.gttest.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.gttest.R
import com.example.gttest.adapter.ContactsAdapter
import com.example.gttest.adapter.GetContactDetails
import com.example.gttest.utils.CONTACT_NAME
import com.example.gttest.utils.CONTACT_NUMBER
import com.example.gttest.viewmodel.ContactsViewModel
import com.kedar.coroutinescontactsfetching.hasPermission
import com.kedar.coroutinescontactsfetching.requestPermissionWithRationale
import kotlinx.android.synthetic.main.activity_contacts.*


class ContactsActivity : AppCompatActivity(), GetContactDetails {
    private val contactsViewModel by viewModels<ContactsViewModel>() //viewModels<ContactsViewModel>()
    private val CONTACTS_READ_REQ_CODE = 100
    lateinit var delegate: GetContactDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        init()
    }

    private fun init() {
        tvDefault.text = "Fetching contacts!!!"
        delegate = this
        val adapter = ContactsAdapter(this, delegate as ContactsActivity)
        rvContacts.adapter = adapter
        contactsViewModel.contactsLiveData.observe(this, Observer {
            tvDefault.visibility = View.GONE
            adapter.contacts = it
        })
        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            contactsViewModel.fetchContacts()
        } else {
            requestPermissionWithRationale(
                Manifest.permission.READ_CONTACTS,
                CONTACTS_READ_REQ_CODE,
                getString(R.string.contact_permission_rationale)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            contactsViewModel.fetchContacts()
        }
    }

    override fun showDeleteDialog(name: String, number: String) {
        Log.d("item clicked" + name, "" + number)
        val intent = Intent()
        intent.putExtra(CONTACT_NAME, name)
        intent.putExtra(CONTACT_NUMBER, number)
        setResult(101, intent)
        finish()
    }

    override fun showError() {
        Toast.makeText(
            this,
            resources.getString(R.string.error_message_for_contact_selection),
            Toast.LENGTH_LONG
        ).show()
    }
}