package com.example.gydealpha.profileBuilder

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.gydealpha.R
import kotlinx.android.synthetic.main.fragment_name_prompt.*


class NamePromptFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_prompt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        button.setOnClickListener {
            if((!TextUtils.isEmpty(input_fname_editText.text.toString())) && (!TextUtils.isEmpty(input_lname_editText.text.toString())) ){
                val bundle = bundleOf(
                    "firstNameArg" to input_fname_editText.text.toString(),
                    "lastNameArg" to input_lname_editText.text.toString())
                navController.navigate(R.id.action_namePromptFragment_to_institutionPromptFragment,bundle)
            }
            else{
                Toast.makeText(activity, "Enter name", Toast.LENGTH_LONG).show()
            }

        }
    }


}
