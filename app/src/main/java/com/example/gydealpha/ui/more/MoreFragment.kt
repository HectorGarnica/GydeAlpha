package com.example.gydealpha.ui.more

import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer

import com.example.gydealpha.R
import com.example.gydealpha.startup.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_more.*
import org.jetbrains.anko.support.v4.startActivity

class MoreFragment : Fragment() {

    private lateinit var moreViewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_more, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingListView.setOnItemClickListener { parent, view, position, id ->


            when (position){
                0 -> {
                    titleMore_textView.text ="About"
                    settingListView.visibility = View.GONE
                    back_to_menu_button.visibility = View.VISIBLE
                    about_layout.visibility = View.VISIBLE
                    back_to_menu_button.setOnClickListener {
                        titleMore_textView.text="Menu"
                        settingListView.visibility = View.VISIBLE
                        back_to_menu_button.visibility = View.GONE
                        about_layout.visibility = View.GONE
                    }
                }
                1 -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity<SignInActivity>()
                    activity?.finish()
                }

            }


        }

    }


}
