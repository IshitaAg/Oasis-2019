package com.dvm.appd.oasis.dbg.more

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
        Contact("Medical Emergency", "+91-9870050422", "+91-9408693984", "+91-7875227790", ""),
        Contact("Raihan Riaz", "Controls", "controls@bits-bosm.org", "+91-9989401360", "${baseImageLink}img/contacts/controls.png"),
        Contact("Amol Dalal", "Sponsorship", "sponsorship@bits-bosm.org", "+91-7020141770 ", "${baseImageLink}img/contacts/spons.png"),
        Contact("Abhinav Kumar Singh", "Reception and Accomodation", "recnacc@bits-bosm.org", "+91-9654298614 ", "${baseImageLink}/contacts/recanec.jpg"),
        Contact("Damanjot Singh", "Publications and Correspondence", "pcr@bits-bosm.org", "+91-8966911000", "${baseImageLink}img/contacts/pcr.png"),
        Contact("Mayank Kulkarni", "Sports Secretary", "sportssecretary@bits-bosm.org", "+91-9929855583", "${baseImageLink}img/contacts/ss.png"),
        Contact("Mansi Mittal", "Joint Sports Secretary", "N/A", "+91-9602775333", "${baseImageLink}img/contacts/jss1.png"),
        Contact("Ankur Jain", "Joint Sports Secretary", "N/A", "+91-9549905512", "${baseImageLink}img/contacts/jss2.png"),
        Contact("Kunal Gupta", "Joint Sports Secretary", "N/A", "+91-7985564751", "${baseImageLink}img/contacts/jss.jpg"),
        Contact("Divyam Goel", "Online Registrations", "webmaster@bits-bosm.org", "+91-9602775333", "${baseImageLink}img/contacts/dvm.jpg")
    )


    override fun getItemCount() = contacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactVHolder(inflater.inflate(R.layout.row_contact_us, parent, false))
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