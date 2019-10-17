package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity

import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.adapters.ContactUsAdapter
import com.dvm.appd.oasis.dbg.more.adapters.CreditsAdapter
import com.dvm.appd.oasis.dbg.more.adapters.DevelopersAdapter
import com.dvm.appd.oasis.dbg.more.dataClasses.Developer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fragment_recycler_view.*

class FragmentRecyclerView : Fragment() {

    lateinit var title: String
    private val picBaseUrl = "https://firebasestorage.googleapis.com/v0/b/bosm-2019.appspot.com/o/"
    private val baseImageLink = "https://www.bits-bosm.org/img/developers/"
    private val developers = listOf(
        Developer("Prarabdh Garg", "App Developer", picBaseUrl + "Prarabdh%20Garg.jpg?alt=media&token=b2514b0b-1dfb-4dd3-8269-04404c671e9f"),
        Developer("Suyash Soni", "App Developer", picBaseUrl + "Suyash%20Soni.jpg?alt=media&token=445e5b1b-427f-4e41-bbdc-71bd581db96e"),
        Developer("Akshat Gupta", "App Developer", picBaseUrl + "Akshat%20Gupta.jpg?alt=media&token=e0a374c6-2c42-4dd6-8d3f-87f98668bb37"),
        Developer("Ishita Aggarwal", "App Developer", picBaseUrl + "Ishita%20Aggarwal.jpg?alt=media&token=a7eba71d-abb9-45e6-9ad9-c932629a8a27"),
        Developer("Pradyumna Bang", "Backend Developer", picBaseUrl + "Pradyumna%20Bang.jpg?alt=media&token=1555b351-8fb2-4167-a32e-fd3e6bed5b5c"),
        Developer("Dushyant Yadav", "Backend Developer", baseImageLink + "dushyant.jpg"),
        Developer("Shivanshu Ayachi", "Backend Developer", picBaseUrl + "raghav.png"),
        Developer("Abhinav Tiwari", "Backend Developer", baseImageLink + "abhinav.jpg"),
        Developer("Divyansh Agarwal", "Backend Developer", picBaseUrl + "raghav.png"),
        Developer("Devansh Agarwal", "UI/UX Designer", picBaseUrl + "Devansh%20Agarwal.jpg?alt=media&token=248de574-72c3-4528-a709-0115f80032cd")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")!!
        }
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_commonRecyclerView_title.text = title
        backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }
        setAdapter()
    }

    private fun setAdapter() {
        when(title) {
            "Contact Us" -> {
                aboutUs.visibility = View.GONE
                recycler_commonRecyclerView.visibility = View.VISIBLE
                recycler_commonRecyclerView.adapter =
                    ContactUsAdapter()
                (recycler_commonRecyclerView.adapter as ContactUsAdapter).notifyDataSetChanged()
            }
            "Developers" -> {
                aboutUs.visibility = View.GONE
                recycler_commonRecyclerView.visibility = View.VISIBLE
                var devs = emptyList<Developer>()
                devs = devs.plus(developers.subList(0,4).shuffled())
                devs = devs.plus(developers.subList(4, (developers.size)))
                recycler_commonRecyclerView.adapter =
                    DevelopersAdapter()
                (recycler_commonRecyclerView.adapter as DevelopersAdapter).developers = devs
                (recycler_commonRecyclerView.adapter as DevelopersAdapter).notifyDataSetChanged()
            }

            "About Us" -> {
                aboutUs.visibility = View.VISIBLE
                recycler_commonRecyclerView.visibility = View.GONE
                aboutUs.text = "Oasis—the cultural fest of BITS Pilani—is nothing but 96 hours of unadulterated thrills comprising music, dance, drama, art, quizzes, fashion, and humour. With a footfall of around 5000+ every year, it is the second-largest cultural festival of India. This year’s theme—neon-noir—is a confluence of the contraries coexisting in harmony. On a superficial level, the theme is inherently contradictory—the concept of both light and darkness manifested simultaneously. On a metaphysical level, however, neon-noir is a commentary on specific socio-cultural dimensions such as urban decay, consumerism, and industrial encroachment. It is characterised by menacing cityscapes with the luminescence accentuating the shadows. If convoluted storylines, crime, and moral fluctuation enrapture you, this Oasis, from 19th to 23rd October, buckle up for the experience of an aesthetic so powerful that you question your very existence in this realm. Live the life of a Taxi Driver and run on blades of talent in the 49th edition of the fest which promises to leave you in a trance caused by the chiaroscuro of shadow and illumination."
            }

            "Credits" -> {
                recycler_commonRecyclerView.visibility = View.VISIBLE
                aboutUs.visibility = View.INVISIBLE
                recycler_commonRecyclerView.adapter = CreditsAdapter()
                (recycler_commonRecyclerView.adapter as CreditsAdapter).notifyDataSetChanged()
            }
        }
    }

}
