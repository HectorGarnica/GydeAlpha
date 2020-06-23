package com.example.gydealpha.profileBuilder

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.gydealpha.R
import com.google.gson.internal.bind.ArrayTypeAdapter
import kotlinx.android.synthetic.main.fragment_institution_prompt.*
import kotlinx.android.synthetic.main.fragment_name_prompt.*


class InstitutionPromptFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var fname: String
    lateinit var lname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fname = arguments!!.getString("firstNameArg").toString()
        lname = arguments!!.getString("lastNameArg").toString()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_institution_prompt, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val universities = resources.getStringArray(R.array.universities)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1,universities)
        input_institution_autoCompleteTextView.setAdapter(adapter)

        navController = Navigation.findNavController(view)

        button2.setOnClickListener {
            if(!TextUtils.isEmpty(input_institution_autoCompleteTextView.text.toString() )){
                val bundle = bundleOf(
                    "firstNameArg" to fname,
                    "lastNameArg" to lname,
                    "institutionArg" to input_institution_autoCompleteTextView.text.toString())
                navController.navigate(R.id.action_institutionPromptFragment_to_majorPromptFragment,bundle)
            }
            else{
                Toast.makeText(activity, "Enter Institution", Toast.LENGTH_LONG).show()
            }




        }
    }

}