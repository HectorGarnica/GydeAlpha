package com.example.gydealpha.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class HomePagerAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {


    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Find a Mentor"
            1 -> "Be a Mentor"
            else -> "Find a Mentor"
        }
    }
    override fun getItem(position: Int): Fragment {
        return when (position){

            0 -> FindMentorsFragment.newInstance()
            1 -> BeAMentorFragment.newInstance()

            else -> FindMentorsFragment.newInstance()

        }

    }

    override fun getCount() = 2
}




