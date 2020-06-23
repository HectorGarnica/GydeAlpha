package com.example.gydealpha.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.gydealpha.R
import com.example.gydealpha.ui.home.chat.MyMentorsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val myFragment = inflater.inflate(R.layout.fragment_home, container, false)
        return myFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewPager(homepageViewPager)
        home_tabs.setupWithViewPager(homepageViewPager)

        home_chat_button.setOnClickListener {
            startActivity<MyMentorsActivity>()
        }

    }

    private fun setUpViewPager(viewpager : ViewPager){
        val adapter =  HomePagerAdapter(childFragmentManager)
        viewpager.adapter= adapter
    }









}