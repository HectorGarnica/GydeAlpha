package com.example.gydealpha.profileBuilder

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.gydealpha.R
import kotlinx.android.synthetic.main.fragment_institution_prompt.*
import kotlinx.android.synthetic.main.fragment_major_prompt.*
import kotlinx.android.synthetic.main.fragment_name_prompt.*


class MajorPromptFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var fname: String
    lateinit var lname: String
    lateinit var institution: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fname = arguments!!.getString("firstNameArg").toString()
        lname = arguments!!.getString("lastNameArg").toString()
        institution = arguments!!.getString("institutionArg").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.i("goku","get String Array")
        return inflater.inflate(R.layout.fragment_major_prompt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val majors = resources.getStringArray(R.array.majors)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1,majors)
        input_major1.setAdapter(adapter)
        input_major2.setAdapter(adapter)

        button3.setOnClickListener {
            if(!TextUtils.isEmpty(input_major1.text.toString()) ){
                val bundle = bundleOf(
                    "firstNameArg" to fname,
                    "lastNameArg" to lname,
                    "institutionArg" to institution,
                    "major1Arg" to input_major1.text.toString() )
                    closeKeyboard()
                navController.navigate(R.id.action_majorPromptFragment_to_mentorSkillPromptFragment,bundle)
            }
            else{
                Toast.makeText(activity, "Enter Major", Toast.LENGTH_LONG).show()
            }

        }






    }

    private fun closeKeyboard() {

            val activity = activity as ProfileBuilderActivity
            val view = activity.currentFocus

            if (view!=null){
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken,0)
            }
    }


}
