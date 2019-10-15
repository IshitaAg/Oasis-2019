package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.dataClasses.Contact
import kotlinx.android.synthetic.main.row_contact_us.view.*


class ContactUsAdapter : RecyclerView.Adapter<ContactUsAdapter.ContactVHolder>() {

    private val baseImageLink = "https://www.bits-bosm.org/"

    private val contacts = listOf(
        Contact("Medical Emergency", "+91-8427878749", "+91-9717548752", "+91-9811695294", ""),
        Contact("Tushar Goel", "Website, App & Online Payments", "webmaster@bits-oasis.org", "+91-9694345679", "${baseImageLink}images/contacts/DVM.png"),
        Contact("Parth Kashikar", "Logistics and Operations", "controls@bits-oasis.org", "+91-9686011770", "${baseImageLink}images/contacts/Parth.jpg"),
        Contact("Adit Chandra", "Sponsorship and Marketing", "sponsorship@bits-oasis.org", "+91-8290801301", "${baseImageLink}images/contacts/adit.png"),
        Contact("Tanvi Gupta", "Reception and Accomodation", "recnacc@bits-oasis.org", "+91-9057232223 ", "${baseImageLink}images/contacts/tanvi.png"),
        Contact("Aditya Pawar", "Online Collaborations and Publicity", "adp@bits-oasis.org", "+91-9829971666", "${baseImageLink}images/contacts/pawar.jpg"),
        Contact("Rahul Bubna", "Student Union President", "President@pilani.bits-pilani.ac.in", "+91-8952824766", "${baseImageLink}images/contacts/rahul.jpg"),
        Contact("V Abishek Balaji", "Student Union General Secretary", "gensec@pilani.bits-pilani.ac.in", "+91-9566142660", "${baseImageLink}images/contacts/abhi.jpg"),
        Contact("Tanmaay Chandak", "Registration, Events & Other Enquiries", "pcr@bits-oasis.org", "+91-9405010405", "${baseImageLink}images/contacts/chandak.png") )


    override fun getItemCount() = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactVHolder(
            inflater.inflate(
                R.layout.row_contact_us,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactVHolder, position: Int) {
        val contact = contacts[position]

        holder.nameLBL.text = contact.name
        holder.roleLBL.text = contact.role
        holder.phoneLBL.text = contact.phone
        holder.emailLBL.text = contact.email

        val glideConfig = RequestOptions()
            .placeholder(R.drawable.ic_person_placeholder)
            .circleCrop()

        Glide.with(holder.nameLBL.context)
            .load(contact.imageLink)
            .apply(glideConfig)
            .into(holder.picIMG)

    }


    class ContactVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val picIMG: ImageView = rootPOV.picIMG
        val nameLBL: TextView = rootPOV.nameLBL
        val roleLBL: TextView = rootPOV.roleLBL
        val phoneLBL: TextView = rootPOV.phoneLBL
        val emailLBL: TextView = rootPOV.emailLBL
    }
}