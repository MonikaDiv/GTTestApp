package com.example.gttest.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gttest.R
import com.example.gttest.model.Contact
import com.example.gttest.view.ContactsActivity
import kotlinx.android.synthetic.main.row_contact.view.*
import kotlinx.android.synthetic.main.row_contact_data.view.*

class ContactsAdapter(
    var context: Context,
    var delegate: ContactsActivity
) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {
    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var contacts = ArrayList<Contact>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(layoutInflater.inflate(R.layout.row_contact, parent, false))
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.itemView) {
            tvContactName.text = contact.name
            llContactDetails.removeAllViews()
            val detail = layoutInflater.inflate(R.layout.row_contact_data, llContactDetails, false)
            if (contact.numbers != null && contact.numbers.size > 0) {
                detail.tvContactData.text = contact.numbers[0]
            }
            llContactDetails.addView(detail)
        }
        holder.itemView.setOnClickListener {
            Log.d("item clicked" + contact.name, "Please select contact" + contact.numbers)
            if (contact.numbers != null && contact.numbers.size > 0) {
                delegate.showDeleteDialog(contact.name, contact.numbers[0])
            } else {
                delegate.showError()
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

interface GetContactDetails {
    fun showDeleteDialog(name: String, number: String)
    fun showError()
}